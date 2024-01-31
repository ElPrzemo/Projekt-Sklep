package com.example.projektsklep.utils;

import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.product.Author;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.enums.ProductType;
import com.example.projektsklep.model.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
public class DbInit {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DbInit(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                  CategoryRepository categoryRepository, AuthorRepository authorRepository,
                  ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    private void postConstruct() {
        // Create roles
        Role adminRole = Role.fromAdminOrUser(AdminOrUser.ADMIN);
        Role userRole = Role.fromAdminOrUser(AdminOrUser.USER);

        // Save roles
        Role savedAdminRole = roleRepository.save(adminRole);
        Role savedUserRole = roleRepository.save(userRole);

        // Create users (you can add more users as needed)
        User user = User.builder()
                .firstName("user")
                .lastName("user")
                .email("user@example.com")
                .passwordHash(passwordEncoder.encode("user"))
                .roles(Set.of(savedUserRole))
                .build();

        User admin = User.builder()
                .firstName("admin")
                .lastName("admin")
                .email("admin@example.com")
                .passwordHash(passwordEncoder.encode("admin"))
                .roles(Set.of(savedAdminRole))
                .build();

        // Save users
        userRepository.save(user);
        userRepository.save(admin);

        List<Category> categories = createCategories();

        // Tworzenie autorów (marki)
        List<Author> authors = createAuthors();

        // Tworzenie produktów
        createProducts(categories, authors);

        // Przypisanie zamówień do użytkowników
        assignOrdersToUsers();
    }

    private List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        // Dodaj kategorie związane z branżą gastronomiczną
        categories.add(new Category("Piece konwekcyjne"));
        categories.add(new Category("Zmywarki przemysłowe"));
        // Dodaj więcej kategorii...
        categories.forEach(categoryRepository::save);
        return categories;
    }

    private List<Author> createAuthors() {
        List<Author> authors = new ArrayList<>();
        // Dodaj marki sprzętu gastronomicznego
        authors.add(new Author("Rational"));
        authors.add(new Author("Electrolux"));
        // Dodaj więcej marek...
        authors.forEach(authorRepository::save);
        return authors;
    }

    private void createProducts(List<Category> categories, List<Author> authors) {
        Random rand = new Random();
        List<String> productNames = List.of("Piec Rational", "Zmywarka Electrolux", "Chłodziarka Coldex", "Mikser Bosh", "Grill Weber"); // Dodaj więcej nazw

        for (int i = 0; i < 100; i++) {
            Category category = categories.get(rand.nextInt(categories.size()));
            Author author = authors.get(rand.nextInt(authors.size()));
            String name = productNames.get(rand.nextInt(productNames.size()));

            Product product = new Product();
            product.setCategory(category);
            product.setAuthor(author);
            product.setTitle(name);
            product.setDescription("Szczegółowy opis " + name);
            product.setMiniature("URL do miniatury " + name);
            product.setPrice(BigDecimal.valueOf(500 + rand.nextDouble() * 1500));
            product.setProductType(ProductType.KITCHEN_EQUIPMENT); // Zmień na odpowiedni typ
            product.setPublished(true);
            product.setDateCreated(LocalDate.now());
            productRepository.save(product);
        }
    }

    private void assignOrdersToUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            for (int i = 1; i <= 20; i++) {
                User user = users.get(i % users.size());
                Order order = new Order();
                order.setAccountHolder(user);
                order.setOrderStatus(OrderStatus.NEW_ORDER);
                order.setDateCreated(LocalDate.now());
                order.setTotalPrice(BigDecimal.valueOf(500 + (i * 30)));
                orderRepository.save(order);
            }
        }
    }
}


        // Create categories (you can add more categories as needed)
//        Category category1 = new Category();
//        category1.setName("Category 1");
//        categoryRepository.save(category1);
//
//        Category category2 = new Category();
//        category2.setName("Category 2");
//        categoryRepository.save(category2);
//
//        Category category3 = new Category();
//        category3.setName("Category 3");
//        categoryRepository.save(category3);
//
//        Category category4 = new Category();
//        category4.setName("Category 4");
//        categoryRepository.save(category4);
//
//        Category category5 = new Category();
//        category5.setName("Category 5");
//        categoryRepository.save(category5);
//
//        // Create authors (you can add more authors as needed)
//        Author author1 = new Author();
//        author1.setName("Author 1");
//        authorRepository.save(author1);
//
//        Author author2 = new Author();
//        author2.setName("Author 2");
//        authorRepository.save(author2);
//
//        Author author3 = new Author();
//        author3.setName("Author 3");
//        authorRepository.save(author3);
//
//        Author author4 = new Author();
//        author4.setName("Author 4");
//        authorRepository.save(author4);
//
//        Author author5 = new Author();
//        author5.setName("Author 5");
//        authorRepository.save(author5);
//
//        // Create products (you can add more products as needed)
//        for (int i = 1; i <= 60; i++) {
//            Product product = new Product();
//            product.setAuthor(i % 5 == 0 ? author1 : author2);
//            product.setCategory(i % 5 == 0 ? category1 : category2);
//            product.setTitle("Product " + i);
//            product.setDescription("Description for Product " + i);
//            product.setMiniature("Miniature URL for Product " + i);
//            product.setPrice(BigDecimal.valueOf(10 + i));
//            product.setProductType(ProductType.DEFAULT_TYPE);
//            product.setPublished(true);
//            product.setDateCreated(LocalDate.now());
//            productRepository.save(product);
//        }
//
//        // Create orders (you can add more orders as needed)
//        for (int i = 1; i <= 20; i++) {
//            Order order = new Order();
//            order.setAccountHolder(i % 2 == 0 ? user : admin);
//            order.setOrderStatus(OrderStatus.NEW_ORDER);
//            order.setDateCreated(LocalDate.now());
//            order.setTotalPrice(BigDecimal.valueOf(100 + i));
//            orderRepository.save(order);
//        }
//    }
//}