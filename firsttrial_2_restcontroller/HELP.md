# Application FirstTrial

This is an Spring Boot 2 Project for Java 11 and Maven 3.3+ 
The code contains best case approaches for fast project adaption.

Its part of a spring boot tutorial containing follwing chapters
1. Create Spring boot 2 project with H2 database, Data Model, Hibernate Repository
2. Add internal CRUD service with basic test capability
3. Create REST Controller, add error output and error handler, create JUnit test for REST controller
4. Add swagger (OpenAPI) documentation for REST controller
5. Add Spring Boot Actuator with Micrometer Metrics, local Prometheus and Graphana severs and customized health check and furhter enhancements

## Content of the enhancements in this repository and best case approaches for project adaption
* The current repository contains enhancements to the basic repository for creation of a REST controller with CRUD (Create, read, update and delete) methods including Junit test capabilities and REST error handling and output error structure. 
*The REST controller contains both a list method with paging capability to handle large amount of records and also a modified like search method as example for customized filter / search methods.
*The project also enhances the input validation based on Hibernate validation.
The input validation in combination with error handling provides 1 ... n errors per request for all validation contraints. Each error contains input parameter name and certain error code for full input validation testability in JUnit tests.

## Customized Exception handling

1. Create beans for standard output structure in case of exception (validation, db, other)
* Error bean
<br> create new class ErrorResponse
<br> add class annotation @XmlRootElement(name = "error")
<br> create class attributes
<pre><code>
		String type for type, message and parameter
		List<String> type for details;
</pre></code><br>
<br> create getters and setters
<br> create constructor for all attribute


* Errors bean
<br> create new class ErrorsResponse
<br> create list attribute for type ErrorResponse: List<ErrorResponse> errorList = new ArrayList<>();
<br> create getter and setter methods			

* Create customized class for error handling
<br> create class CustomExceptionHandler extending Spring ResponseEntityExceptionHandler class
<br> Add class annotations @SuppressWarnings({"unchecked","rawtypes"}) and @ControllerAdvice
<br> Add single method per exception type to create output as errors list
<pre><code>
			@ExceptionHandler(Exception.class)
			public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
			(...)
</pre></code>
<br>
<pre><code>			
			@ExceptionHandler(RecordNotFoundException.class)
			public final ResponseEntity<Object> handleUserNotFoundException(RecordNotFoundException ex, WebRequest request) {
			(...)
			}
</pre></code>			
<br>
<pre><code> 
			 @Override //Override Method in ResponseEntityExceptionHandler
			protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {   
			(...)
			}
</pre></code>
<br>
<pre><code>			
			Example for unspecified exception
			@ExceptionHandler(Exception.class)
			public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
				List<String> details = new ArrayList<>();
				details.add(ex.getLocalizedMessage());
				List<ErrorResponse> errors = new ArrayList<>();
				errors.add(new ErrorResponse("50000", "Server Error", null, details));
				return new ResponseEntity(errors, HttpStatus.INTERNAL_SERVER_ERROR);
			}
</pre></code>
<br>

## Create REST controller class
* create new restcontroller package
* create new class CustomerController
* Add class annotations
<pre><code> 
			@RestController
			@RequestMapping("/customers") - includes base path for customer REST resource
</pre></code><br>

* Link functional service 
<pre><code>
			@Autowired
			CustomerService service;
</pre></code><br>

* Add methods for customer REST resource, optional with sub path, (MIME type = JSON, if not explicitely specified), compare code example for details
<pre><code>
			@GetMapping()
			public ResponseEntity<List<CustomerEntity>> getAllcustomersWithPagination(
				@RequestParam(defaultValue = "0") Integer pageNo, 
				@RequestParam(defaultValue = "10") Integer pageSize,
				@RequestParam(defaultValue = "id") String sortBy
				) {
				--> Includes paging capability to avoid performance issues with growing record amount
				
			@GetMapping("/{id}")
			public ResponseEntity<CustomerEntity> getcustomerById(@PathVariable("id") Long id) throws RecordNotFoundException {
													
			@GetMapping("/name/{name}")
			public ResponseEntity<List<CustomerEntity>> getCustomersByLastNameLike(@PathVariable("name") Optional<String> name) { 
        
			@PostMapping
			public ResponseEntity<CustomerEntity> createOrUpdatecustomer(@Valid @RequestBody CustomerEntity customer) throws RecordNotFoundException {
			--> Method is used for both record creation and update. This is a typical approach to combine both in HTTP POST method instead of 2 methods for creation (HTTP POST) and update (HTTP PUT)
			--> See @Valid annotation for input bean validation
 
			@DeleteMapping("/{id}")
			public ResponseEntity deletecustomerById(@PathVariable("id") Long id) 
															throws RecordNotFoundException {															
</pre></code><br>
<br> Implement content for the methods by reusing CustomerService methods

* Start Spring Boot application and try following (GET) requests in browser:
<br> http://localhost:8080/customers (getAllcustomersWithPagination)
<br> http://localhost:8080/customers/3 (getcustomerById)
		
