import com.mongodb.*;
import com.mongodb.util.JSON;

import java.net.UnknownHostException;
import java.util.*;

import static com.sun.tools.doclint.Entity.and;
import static com.sun.tools.doclint.Entity.prod;

/**
 * Created by anton on 2017-05-22.
 */
public class Main {
    List <Product>productList = new ArrayList();
    int totalprice = 0;
    MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    DB database = mongoClient.getDB("BeaverCoffee");
    Scanner scan = new Scanner(System.in);
    public Main() throws UnknownHostException {

        System.out.println("Welcome to BeaverCoffee - What do you wanna do?");
        while (true) {
            System.out.println("1. Place an order!\n2. Update an order!\n3. Delete an order!\n4. Add member!\n5. Employees\n6. Stock\n7. Get sales\n8. Who sold what?");
            int input = scan.nextInt();
            switch (input) {
                case 1: placeOrder();
                    break;
                case 2: updateOrder();
                    break;
                case 3: deleteOrder();
                    break;
                case 4: addMember();
                    break;
                case 5: employees();
                    break;
                case 6: stock();
                    break;
                case 7: getSales();
                    break;
                case 8: whoSoldThat();
                    break;
                default: System.out.print("No, wrong");
                    break;
            }
        }
    }

    private void whoSoldThat() {

    }

    private void getSales() {

    }

    private void stock() {

    }

    private void employees() {
        DBCollection employees = database.getCollection("Employee");
        DBCursor cursor = employees.find();

        while (cursor.hasNext()){
            System.out.println(cursor.next());
        }

    }

    private void addMember() {
    }

    private void deleteOrder() {

    }

    private void updateOrder() {

    }

    private void placeOrder() {
        boolean theresMore = true;
        System.out.println("What do you like to order?");
        while (theresMore) {
            System.out.println("1. Whole-Bean Coffee\n2. Brewed Coffee\n3. Espresso\n4. Latte\n5. Capucccino\n6. Hot Chocolate\n7. Noting more, thanks!");
            int input1 = scan.nextInt();
            switch (input1) {
                case 1:
                    whatRoast();
                    break;
                case 2:
                    addToProductList(new Product("Brewed Coffee", "", 20));
                    break;
                case 3:
                    addToProductList(new Product("Espresso", "", 20));
                    break;
                case 4:
                    whatMilk("Latte");
                    break;
                case 5:
                    whatMilk("Capuccino");
                    break;
                case 6:
                    System.out.println("Would you like wipped cream?\n1. Yes!\n2. No!");
                    int input2 = scan.nextInt();
                    if (input2 == 1) {
                        addToProductList(new Product("Hot Chocolate (Cocoa Mix)", "Wipped Cream" ,15));
                    } else if (input2 == 2) {
                        addToProductList(new Product("Hot Chocolate (Cocoa Mix)", "", 15));
                    }
            }
            System.out.println("Do you want anything more?\n1. Yes!\n2. No!");
            int input3 = scan.nextInt();
            if(input3 == 2){
                DBCollection order = database.getCollection("Order");
                BasicDBObject newOrder = new BasicDBObject();
                BasicDBList proList = new BasicDBList();
                for(int j = 0; j < productList.size(); j++){
                    proList.add(new BasicDBObject("name", productList.get(j).getName()).append( "mod", productList.get(j).getMod()).append("price", productList.get(j).getPrice()));
                }
                newOrder.put("productlist", proList);
                newOrder.put("currency", "sek");
                order.insert(newOrder);
                theresMore = false;
                System.out.println("You've ordered ");
                for(int i = 0; i < productList.size(); i++){
                    System.out.println(productList.get(i).getName() + ", " + productList.get(i).getMod());
                }
                System.out.println("That'll be " + totalprice + "kr.\n\n");
                productList = new ArrayList();
                totalprice = 0;
            }
        }
    }


    private void whatMilk(String drink) {
        System.out.println("What kind of steamed milk would you like for your " + drink + "?");
        System.out.println("1. Skim Milk\n2. Soy Milk\n3. Whole Milk,\n4. 2%Milk\n");
        int input = scan.nextInt();
        switch (input) {
            case 1: addToProductList(new Product(drink, "Skim Milk", 20));
                break;
            case 2: addToProductList(new Product(drink, "Soy Milk", 20));
                break;
            case 3: addToProductList(new Product(drink, "Whole Milk", 20));
                break;
            case 4: addToProductList(new Product(drink, "2% Milk", 20));
        }
    }

    private void whatRoast() {
        System.out.println("What roast would you like for Whole-Bean Coffe?");
        System.out.println("1. Espresso Roast\n2. Whole Bean French Roast\n3. Qhole Bean Ligt Roast");
        int input = scan.nextInt();
        switch (input) {
            case 1:
                addToProductList(new Product("Whole-Bean Coffee", "Espresso Roast" ,25));
                break;
            case 2:
                addToProductList(new Product("Whole-Bean Coffee", "Whole Bean French Roast" ,25));
                break;
            case 3:
                addToProductList(new Product("Whole-Bean Coffee", "Qhole Bean Ligt Roast" ,25));
                break;
        }
    }

    private void addToProductList(Product product) {
        productList.add(product);
        totalprice += product.price;
        System.out.println("Added an " + product.name + ", " + product.mod + " that costed " + product.price + " kr.");
    }

    public static void main(String args[]) throws UnknownHostException {
        new Main();

    }
    class Product{
        int price;
        String name, mod;
        private Product(String name, String mod, int price){
            this.name = name;
            this.mod = mod;
            this.price = price;
        }
        public int getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }

        public String getMod() {
            return mod;
        }
    }
}
