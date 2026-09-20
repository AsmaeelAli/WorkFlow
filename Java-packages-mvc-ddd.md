# توزيع البكجات (Packages) في مشاريع الجافا: MVC مقابل DDD

## 1. مقدمة

تنظيم البكجات في أي مشروع Java/Spring Boot ليس مجرد شكل جمالي، بل هو قرار معماري يؤثر على:

- سهولة الصيانة والتوسّع
- درجة الترابط (Coupling) بين الطبقات
- قدرة الفريق على العمل بالتوازي دون تعارضات كبيرة
- وضوح حدود المسؤوليات (Separation of Concerns)

يوجد نمطان شائعان لتنظيم البكجات:

1. **Package by Layer (النمط التقليدي المرتبط بـ MVC)**
2. **Package by Feature/Domain (المرتبط بـ DDD)**

---

## 2. النمط الأول: Layered Architecture (MVC التقليدي)

### 2.1 الفكرة

يتم تقسيم المشروع حسب **الطبقة التقنية** (Layer) بغض النظر عن الميزة أو الدومين. كل طبقة تحتوي على كل الكلاسات من نوعها
لكل الميزات مجتمعة.

### 2.2 هيكل البكجات

```
com.company.projectname
│
├── controller
│   ├── UserController.java
│   ├── OrderController.java
│   └── ProductController.java
│
├── service
│   ├── UserService.java
│   ├── OrderService.java
│   └── ProductService.java
│
├── repository
│   ├── UserRepository.java
│   ├── OrderRepository.java
│   └── ProductRepository.java
│
├── model (أو entity)
│   ├── User.java
│   ├── Order.java
│   └── Product.java
│
├── dto
│   ├── UserRequestDto.java
│   ├── UserResponseDto.java
│   └── OrderDto.java
│
├── exception
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
│
└── config
    └── SecurityConfig.java
```

### 2.3 مثال عملي

```java
// controller/UserController.java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto request) {
        return ResponseEntity.ok(userService.createUser(request));
    }
}

// service/UserService.java
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(UserRequestDto request) {
        User user = new User(request.getName(), request.getEmail());
        userRepository.save(user);
        return new UserResponseDto(user.getId(), user.getName());
    }
}

// repository/UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
}

// model/User.java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    // constructors, getters/setters
}
```

### 2.4 مزايا وعيوب هذا النمط

| المزايا                                  | العيوب                                                        |
|------------------------------------------|---------------------------------------------------------------|
| بسيط وسهل الفهم للمبتدئين                | مع نمو المشروع، كل بكج يصبح ضخمًا جدًا (Fat Package)            |
| مناسب للمشاريع الصغيرة والـ CRUD البسيط  | صعوبة تتبع منطق ميزة واحدة (تتوزع ملفاتها على 5 بكجات مختلفة) |
| يتماشى تمامًا مع بنية Spring MVC القياسية | ترابط ضمني عالي بين الميزات المختلفة (كل شيء "مكشوف" للجميع)  |
| توثيق ودروس كثيرة تعتمد عليه             | لا يعكس حدود الدومين (Domain Boundaries) الحقيقية             |

> **مصدر موثوق:** هذا النمط موثّق
> في [Spring Boot's official guide "Structuring Your Code"](https://docs.spring.io/spring-boot/reference/using/structuring-your-code.html)
> والذي يوصي بوضع الكلاس الرئيسي (Application class) في الحزمة الجذرية (root package) بحيث تكون كل الحزم الفرعية تحتها.

---

## 3. النمط الثاني: Package by Feature / Domain-Driven Design (DDD)

### 3.1 الفكرة

بدلاً من التقسيم حسب الطبقة التقنية، يتم التقسيم حسب **الدومين أو الميزة (Bounded Context)**. كل بكج يمثل وحدة عمل
متكاملة قائمة بذاتها (تحتوي Controller وService وRepository وEntity الخاصة بها).

هذا يعكس أحد المبادئ الجوهرية في DDD: **Bounded Context** — أي أن كل دومين له نموذجه الخاص، ولغته الخاصة (Ubiquitous
Language)، وحدوده الواضحة مع الدومينات الأخرى.

### 3.2 هيكل البكجات

```
com.company.projectname
│
├── user
│   ├── UserController.java
│   ├── UserService.java
│   ├── UserRepository.java
│   ├── User.java                 (Aggregate Root / Entity)
│   ├── UserRequestDto.java
│   └── UserResponseDto.java
│
├── order
│   ├── OrderController.java
│   ├── OrderService.java
│   ├── OrderRepository.java
│   ├── Order.java                (Aggregate Root)
│   ├── OrderItem.java            (Entity داخل نفس الـ Aggregate)
│   ├── OrderStatus.java          (Value Object / Enum)
│   └── OrderDto.java
│
├── product
│   ├── ProductController.java
│   ├── ProductService.java
│   ├── ProductRepository.java
│   └── Product.java
│
├── shared (أو common)
│   ├── exception
│   │   ├── ResourceNotFoundException.java
│   │   └── GlobalExceptionHandler.java
│   └── config
│       └── SecurityConfig.java
│
└── ProjectNameApplication.java
```

### 3.3 مثال عملي بمنهجية DDD (مع مفاهيم Aggregate و Value Object)

```java
// order/Order.java  -> Aggregate Root
@Entity
public class Order {

    @Id @GeneratedValue
    private Long id;

    @Embedded
    private CustomerId customerId;      // Value Object

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // منطق الدومين يعيش داخل الـ Entity نفسه، وليس في الـ Service فقط
    public void addItem(Product product, int quantity) {
        if (this.status != OrderStatus.DRAFT) {
            throw new IllegalStateException("لا يمكن إضافة عناصر لطلب مؤكد");
        }
        this.items.add(new OrderItem(product.getId(), quantity, product.getPrice()));
    }

    public void confirm() {
        if (items.isEmpty()) {
            throw new IllegalStateException("لا يمكن تأكيد طلب فارغ");
        }
        this.status = OrderStatus.CONFIRMED;
    }
}

// order/CustomerId.java -> Value Object
public record CustomerId(Long value) {
    public CustomerId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Customer ID غير صالح");
        }
    }
}

// order/OrderService.java -> Application Service
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderDto placeOrder(PlaceOrderCommand command) {
        Order order = new Order(new CustomerId(command.customerId()));

        command.items().forEach(item -> {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("المنتج غير موجود"));
            order.addItem(product, item.quantity());
        });

        order.confirm();
        orderRepository.save(order);
        return OrderDto.fromEntity(order);
    }
}
```

### 3.4 ملاحظة معمارية مهمة

لاحظ الفرق الجوهري: في MVC التقليدي، الـ `Order` عادة يكون كلاس بيانات فقط (Anemic Model) والمنطق كله في الـ Service.
أما في DDD، الـ `Order` **يحمي قواعده الخاصة بنفسه** (Rich Domain Model) — هذا مبدأ اسمه **"Tell, Don't Ask"**.

### 3.5 مزايا وعيوب DDD Package by Feature

| المزايا                                                     | العيوب                                                                       |
|-------------------------------------------------------------|------------------------------------------------------------------------------|
| كل ميزة معزولة ومكتفية ذاتيًا (High Cohesion)                | يحتاج فهمًا أعمق للمبادئ المعمارية (Aggregate, Value Object, Bounded Context) |
| سهولة حذف/فصل ميزة كاملة لاحقًا لتحويلها إلى Microservice    | قد يبدو "تكرارًا" ظاهريًا للمبتدئين (كل بكج فيه Controller/Service خاص به)     |
| منطق العمل محمي داخل الـ Domain Model نفسه                  | يتطلب انضباطًا في تعريف حدود كل Bounded Context                               |
| يتماشى مع مبادئ Clean Architecture / Hexagonal Architecture | Overkill لمشروع صغير جدًا أو CRUD بسيط                                        |

> **مصدر موثوق:** هذا الأسلوب موثّق بشكل موسّع في كتاب *"Domain-Driven Design: Tackling Complexity in the Heart of
Software"* لـ Eric Evans، وكذلك في مقالات مرجعية
> مثل [Baeldung - "Package by Feature vs. Package by Layer"](https://www.baeldung.com/java-package-by-feature-vs-package-by-layer).

---

## 4. جدول مقارنة سريع

| المعيار                          | Package by Layer (MVC)                  | Package by Feature (DDD)                    |
|----------------------------------|-----------------------------------------|---------------------------------------------|
| معيار التقسيم                    | النوع التقني (Controller/Service/Repo)  | الدومين/الميزة (User, Order, Product)       |
| حجم المشروع المناسب              | صغير إلى متوسط                          | متوسط إلى كبير ومعقّد                        |
| سهولة تتبع ميزة كاملة            | صعبة (موزعة على عدة بكجات)              | سهلة (كل شيء في بكج واحد)                   |
| قابلية التحويل لـ Microservices  | صعبة نسبيًا                              | سهلة نسبيًا                                  |
| مكان منطق العمل (Business Logic) | غالبًا في الـ Service فقط (Anemic Model) | داخل الـ Entity/Aggregate نفسه (Rich Model) |
| منحنى التعلم                     | منخفض                                   | متوسط إلى مرتفع                             |

---

## 5. توصية عملية

- **مشروع تعليمي/بسيط (CRUD فقط):** استخدم Package by Layer، فهو الأسرع والأوضح.
- **مشروع حقيقي متوسط الحجم يُتوقع نموه:** ابدأ بـ Package by Feature حتى لو لم تطبّق DDD كاملة (Aggregates, Value
  Objects)، لأن إعادة الهيكلة لاحقًا مكلفة.
- **نظام معقد بمنطق عمل ثقيل (Fintech, Insurance, ERP):** اعتمد DDD الكاملة مع تقسيم إضافي داخل كل Feature إلى:
  ```
  order
  ├── domain        (Entities, Value Objects, Domain Services)
  ├── application    (Use Cases / Application Services)
  ├── infrastructure (Repository Implementation, External APIs)
  └── web            (Controllers, DTOs)
  ```
  وهذا ما يُعرف بـ **Hexagonal / Onion Architecture** المطبّقة على مستوى كل Bounded Context.

---

## 6. المصادر المرجعية

- Spring Boot Official Docs — Structuring Your Code
- Eric Evans — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vaughn Vernon — *Implementing Domain-Driven Design*
- Baeldung — Package by Feature vs. Package by Layer