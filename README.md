Welcome to my Employee Management System (created with Java + SQLite)

How to RUN: (Eclipse)
1. Open the project.
2. NECESSARY: Make sure the SQLite JDBC jar is on the build path.
   - `lib/sqlite-jdbc-xxxx.jar` → Build Path → Add to Build Path
3. Run `Main.java` as a Java Application.

## What It Does:
This is a Command line Employee Management System connected to an SQLite database. Data saves to `database.db` so it stays after you close the program! This is a barebones program designed solely to practice implementation of CRUD operations (Create, Read, Update, Delete).

## Menu
1) Create  
2) Read by ID  
3) Read all  
4) Read active only  
5) Update  
6) Delete  
0) Exit

## IDs
IDs created in this program are designed to be a random 5 digit public ID** (`public_id`). The public ID is designed to create a random 5 digit number each time without duplication or incrementally increasing.

## Database
- File: `database.db` (project root)
- Tables are created automatically at startup (`Database.init()`)

Employee fields:
- `employee_id` (internal key)
- `public_id` (5-digit, shown to user, unique)
- `full_name`
- `email` (unique)
- `job_title`
- `department_id` (optional)
- `active`

## Exceptions used
- `InvalidInputException` (bad input like invalid email)
- `DuplicateRecordException` (duplicate email / id)
- `RecordNotFoundException` (id not found)
