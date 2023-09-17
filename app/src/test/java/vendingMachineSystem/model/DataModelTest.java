package vendingMachineSystem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataModelTest {

    @BeforeEach
    void prepare(){
        Database testdb = Database.getInstance();
        testdb.connect("test_database.db");
        try {
            testdb.dropTable("Products");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void allProductsOne(){
        String pop = """
			INSERT INTO Products(id, name, category, quantity, price) 
				VALUES 
					(1, '1', 'Drinks', 5, 1.0),
					(2, '2', 'Chips', 5, 2.00),
					(3, '3', 'Candies', 5, 3.00)
			;
			""";
        List<Product> actual;
        List<Product> expected = new ArrayList<Product>();

        Database testdb = Database.getInstance();
        testdb.connect("test_database.db");
        try {
            testdb.doStatement(pop);
            actual = testdb.getAllProducts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //check size
        assertEquals( actual.size(), 3);

        for ( int pop_n = 0; pop_n <3; pop_n++ ){
            expected.add( new Product(1, "1", "Drinks", 5, 1.0F) );
            expected.add( new Product(2, "2", "Chips", 5, 2.0F) );
            expected.add( new Product(3, "3", "Candies", 5, 3.0F) );
        }

        // now we check if they match
        for ( int pop_n = 0; pop_n <actual.size(); pop_n++ ){
            assertEquals( expected.get(pop_n).getId(), actual.get(pop_n).getId() );
            assertEquals( expected.get(pop_n).getName(), actual.get(pop_n).getName() );
            assertEquals( expected.get(pop_n).getCategory(), actual.get(pop_n).getCategory() );
            assertEquals( expected.get(pop_n).getQuantity(), actual.get(pop_n).getQuantity() );
            assertEquals( expected.get(pop_n).getPrice(), actual.get(pop_n).getPrice() );
        }

    }
}
