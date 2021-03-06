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
<br> create class attributes (String type for type, message and parameter + List<String> type for details)
<br> create getters and setters
<br> create constructor for all attributes

* Errors bean
<br> create new class ErrorsResponse
<br> create list attribute for type ErrorResponse: List<ErrorResponse> errorList = new ArrayList<>();
<br> create getter and setter methods			

2.  Create customized class for error handling
<br> create class CustomExceptionHandler extending Spring ResponseEntityExceptionHandler class
<br> Add class annotations @SuppressWarnings({"unchecked","rawtypes"}) and @ControllerAdvice
<br> Add single method per exception type to create output as errors list
<pre><code>
	@ExceptionHandler(Exception.class)
	public final ResponseEntity&lt;Object&gt; handleAllExceptions(Exception ex, WebRequest request) {
	(...)
</pre></code>
<br>
<pre><code>			
	@ExceptionHandler(RecordNotFoundException.class)
	public final ResponseEntity&lt;Object&gt; handleUserNotFoundException(RecordNotFoundException ex, WebRequest request) {
	(...)
	}
</pre></code>			
<br>
<pre><code> 
	 @Override //Override Method in ResponseEntityExceptionHandler
	protected ResponseEntity&lt;Object&gt; handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {   
	(...)
	}
</pre></code>
<br>
<pre><code>			
	Example for unspecified exception
	@ExceptionHandler(Exception.class)
	public final ResponseEntity&lt;Object&gt; handleAllExceptions(Exception ex, WebRequest request) {
		List&lt;String&gt; details = new ArrayList&lt;&gt;();
		details.add(ex.getLocalizedMessage());
		List&lt;ErrorResponse&gt; errors = new ArrayList&lt;&gt;();
		errors.add(new ErrorResponse(&quot;50000&quot;, &quot;Server Error&quot;, null, details));
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
		
## Create JUnit test cases for REST Controller methods
1. Add dependency for RestAssured to project pom file:
<pre><code>
	&lt;dependency&gt;
		&lt;groupId&gt;io.rest-assured&lt;/groupId&gt;
		&lt;artifactId&gt;rest-assured&lt;/artifactId&gt;
		&lt;scope&gt;test&lt;/scope&gt;
	&lt;/dependency&gt;
</pre></code>

2. Create customer bean class with same attributes for test purpose only in test package

3. Enhance JUnit class 
<pre><code>
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static long identifier; //State between test methods
</pre></code>
<br> Check Docu [RestAssured (Behaviour driven testing) how to create REST test cases in Given-When-Then structure](http://rest-assured.io/)
<br> Create JUnit test methods - each per REST controller method (happy case)
<br> Create JUNIT test methods for input validation of mandatory and length constraints (compare source code examples)
<br> Run JUnit tests
	
## If familiar with Postman create single REST test cases

