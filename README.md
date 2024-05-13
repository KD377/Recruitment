## README for Spring Boot Application

### Pre-requisites:
- **Java Development Kit (JDK):** Ensure you have JDK installed.
- **Maven:** Ensure Maven is installed on your machine.
- **Git:** Clone the repository using the following command:
git clone <repository-url>

### To Run the Application:

1. **Navigate to Project Directory:**
cd path/to/directory/with/pom.xml
2. **Build the Project, Run Tests:**
mvn clean install
3. **Start the Application:**
mvn spring-boot:run
4. **Generate JaCoCo report:**
mvn clean install
then go to target/site/index.html in your project directory

### Accessing H2 Console:

- **URL:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **Default JDBC URL:** `jdbc:h2:mem:testdb` (Check your `application.properties` to confirm)
- **Username:** `intern`
- **Password:** (Leave blank if not configured)

### REST Services:

#### Create New Product:
- **POST** `localhost:8080/api/products`
- **Sample Request Body:**
```json
{
   "name": "coffee machine",
   "description": "Makes great coffee",
   "currency": "USD",
   "regularPrice": 15.0
}
```
#### Get All Products:
-**GET** `localhost:8080/api/products`
#### Update Product Data:

-**PUT** `localhost:8080/api/products/{productId}`
-**Sample Request Body:**
```json
{
    "name": "coffee machine",
    "description": "test",
    "regularPrice": 1,
    "currency": "EUR"
}
```
#### Create new Promo Code 
-**POST** `localhost:8080/api/promo-code`
-**Sample request body :**
```json
{
  "code": "TEST", 
  "expirationDate": "2024-05-14", 
  "discountValue": 5.0, 
  "currency": "USD", 
  "maxUsages": 15.0
}
```
-**NOTE: If you want the code to be valid until 13.05.2024 23:59 always provide the next day (14.05.2024)**
#### Get all promo codes
-**GET** `localhost:8080/api/promo-code`
#### Get one promo code's details by providing the promo code
-**GET** `localhost:8080/api/promo-code/{CODE}`
#### Get the discount price by providing a product and a promo code.
-**POST** `localhost:8080/api/discount`
-**Sample query:**
```json
{
    "productId": 1,
    "code": "TEST"
}
```
#### Simulate purchase
-**POST** `localhost:8080/api/purchase`
-**Sample query:**
```json
{
    "productId": 1,
    "code": "TEST"
}
```
#### Sales report by currency
-**GET** `localhost:8080/api/sales-report`
