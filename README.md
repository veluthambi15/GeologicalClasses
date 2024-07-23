# Geological Classes Application

## Overview
This application manages geological classes and sections, providing CRUD operations for both entities. It utilizes a PostgreSQL database for storage and Spring Boot for backend services.

# Instructions to Run the Application
### Prerequisites
- Java JDK 11 or higher
- Maven
- PostgreSQL Database

## Steps
### Clone the Repository:

git clone https://github.com/yourusername/GeologicalClasses.git

### Configure Database Connection:

Update application.properties with your PostgreSQL database credentials.

#### properties

- spring.application.name=GeologicalClasses

- spring.datasource.url=jdbc:postgresql://localhost:5432/geological_class
- spring.datasource.username=postgres
- spring.datasource.password=postgres
- spring.jpa.hibernate.ddl-auto=update

- spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

## Accessing the Endpoints in Postman:
### Configure Security:

By default, the application uses HTTP Basic authentication.

- Username: user
- Password: password

Use these credentials to authenticate your requests in Postman.

## Run the Application:

Open the project in your IDE and run it as a Spring Boot application.

The application will be accessible at:

http://localhost:8080


## API Endpoints:
### Section API

- Create Section: POST /sections
- Get Section by ID: GET /sections/{id}
- Update Section: PUT /sections/{id}
- Delete Section: DELETE /sections/{id}
- List Sections: GET /sections

### Geological Class API

- Create Geological Class: POST /geological-classes
- Get Geological Class by ID: GET /geological-classes/{id}
- Update Geological Class: PUT /geological-classes/{id}
- Delete Geological Class: DELETE /geological-classes/{id}
- List Geological Classes: GET /geological-classes

## Example Request Body for Creating a Section:

### json
{
  "name": "Section 1"
}

#### Example Request Body for Creating a Geological Class:

### json
{
  "name": "Geo Class 1",
  "code": "GC1",
  "sectionId": 1
}

### Import and Export Endpoints

- Import Data: POST /import
  ##### Example Request Body:

  {
    "file": "<base64_encoded_file_content>"
  }

- Export Data: GET /export
  Response: The response will be a job id of exported data which stored in local memory.

- Export Data: GET /export/{id}/file
  Response: The response will be a file (.xlsx) containing the exported data.

## Design Choices
- Database: PostgreSQL is used for reliable and scalable storage.
- Entity Relationships: Section and GeologicalClass entities are connected with a one-to-many relationship.
- Caching: Results of frequently accessed data can be cached for improved performance (consider integrating caching if needed).
- Error Handling: Detailed error messages and validation are implemented to handle invalid inputs and database constraints.

## Technologies Used
- Java Spring Boot: For building the RESTful web service.
- PostgreSQL: For relational database management.
- Maven: For dependency management and build automation.
