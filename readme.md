# Munro Records API
## Description
API is using imported CSV file as a sorce of records. CSV file is imported automatically
in start of application. I used SpringBoot, TDD, build using Maven, controller
is described by Swagger. 

Server is running on port 8000, base content url is **"../munro/api"**, the only
controller method is accessible on url **"../munro/api/records"**, the parameters of method are
described by Swagger. To access swagger-ui is used base url **"../munro/api/swagger-ui"** 
## Limitations
* Hard coded parameters, in real application would use property/yarn files/enums for it
* No integration tests
* Validation is hardcoded
## Sugestions
* I would use in memory SQL for filtering results
* I would remove minHeight >= maxHeight validation from REST controller, and leave it on frontend. If you put wrong values, result is empty. 
