# Terrain Rental — Spring Boot + MySQL

A REST API for renting football fields (**terrains**). Built with Spring Boot 3, Spring Data JPA, and MySQL.

## Entities
`User` (supporting table for foreign keys), `Terrain`, `TerrainImage`, `Booking`, `Payment`, `Review`, `Favorite`.

Each model has: an Entity, a Spring Data JPA Repository, and a REST Controller. Sample data is inserted by `DataSeeder` on first startup.

## Requirements
- Java 17+
- Maven 3.9+
- MySQL 8+

## Setup (Ubuntu)
```bash
# 1. Install prerequisites
sudo apt update
sudo apt install -y openjdk-17-jdk maven mysql-server

# 2. Create the database (or rely on createDatabaseIfNotExist in the URL)
sudo mysql -e "CREATE DATABASE IF NOT EXISTS terrain_rental;"
```

Update DB credentials in `src/main/resources/application.properties` if yours differ
(`spring.datasource.username` / `spring.datasource.password`).

## Run
```bash
mvn spring-boot:run
```
App starts on http://localhost:8080

## Test
```bash
mvn test
```
Tests use an in-memory **H2** database, so no MySQL server is required to run them.

## REST Endpoints (CRUD)
| Resource       | Base path               |
|----------------|-------------------------|
| Terrains       | `/api/terrains`         |
| Terrain Images | `/api/terrain-images`   |
| Bookings       | `/api/bookings`         |
| Payments       | `/api/payments`         |
| Reviews        | `/api/reviews`          |
| Favorites      | `/api/favorites`        |

Each supports `GET /`, `GET /{id}`, `POST /`, `PUT /{id}` (except favorites), `DELETE /{id}`.

Example:
```bash
curl http://localhost:8080/api/terrains
```

## Notes on the schema
- `User` is not in the original spec, but `owner_id`, `renter_id`, and `user_id`
  reference a `users` table, so a minimal `users` entity backs those foreign keys.
- `Terrain.main_image_id` and `TerrainImage.terrain_id` form a circular reference;
  the seeder creates the terrain first, then its images, then links the main image.
- Foreign keys use `ON DELETE CASCADE` per the spec (Hibernate `@OnDelete`).
- `created_at` / `updated_at` are auto-managed by Hibernate `@CreationTimestamp` /
  `@UpdateTimestamp` (the "default NOW()" behaviour).
