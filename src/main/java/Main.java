import com.mongodb.*;
import com.mongodb.util.JSON;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static com.sun.tools.doclint.Entity.and;
import static com.sun.tools.doclint.Entity.prod;

/**
 * Created by anton on 2017-05-22.
 */
public class Main {
    MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    Scanner scan = new Scanner(System.in);
    DB database = mongoClient.getDB("BeaverCoffee");
    DBCollection col = database.getCollection("Order");

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
                case 4:
                    addMember();
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
        System.out.println("\n\nSo... You want to add a member? \nEnter the becoming members SSN number please.");
        Scanner input = new Scanner(System.in);
        String ssn = input.next();

        DBCollection customers = database.getCollection("Customer");
        DBCursor cursor = customers.find();

        while (cursor.hasNext()) {
            DBObject aMember = cursor.next();
            if(aMember.get("SSN").equals(ssn) && aMember.get("club_member").equals("false")){
                System.out.println("Are you sure you want to add " + ssn +  " as a member? (Yes or No)");
                String answer = input.next().toLowerCase();
                if(answer.equals("yes")){

                    BasicDBObject newDocument = new BasicDBObject();
                    newDocument.append("$set", new BasicDBObject().append( "club_member", "true"));
                    BasicDBObject searchQuery = new BasicDBObject().append("SSN", ssn);
                    customers.update(searchQuery, newDocument);

                    System.out.println(aMember.get("SSN") + " I now a member!");

                }
            } else if(aMember.get("SSN").equals(ssn)) {
                System.out.println(ssn + " Is already a member!");
            }
        }

    }

    private void deleteOrder() {

    }

    private void updateOrder() {
        System.out.print("Please tell me the ID of the order number");
        int ordernumber = scan.nextInt();

        DBCollection collection = database.getCollection("Order");
        DBObject query = new BasicDBObject("_id", "jo");
        DBCursor cursor = collection.find(query);
        DBObject order = cursor.one();

        System.out.print((String)order.get("id"));
        BasicDBObject newOrder = new BasicDBObject();
        System.out.print("What do you want to do?\n1. Edit product list\n2. Edit currency");
        int in = scan.nextInt();
        switch (in){
            case 1:
                System.out.print("Exsisting products in the list:");
                List l = (List)order.get("product_list");
                for(int i = 0; i <= l.size(); i++){
                    System.out.println("" + i + ". " + l.get(i));
                }
                System.out.println("Select what product to edit:");
                int product = scan.nextInt();
                l.remove(product);
                
                break;
            case 2: System.out.print("Please type the currency used: ");
                    String curr = scan.nextLine();
                    newOrder.append("$set", new BasicDBObject().append("currency", curr));
                    break;
            default: System.out.print("Nothing chosen, aborting editing\n");
                break;
        }

        collection.update(query, newOrder);
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
        System.out.println("Added an " + product.name + ", " + product.mod + " that costed " + product.price);
    }

    public static void main(String args[]) throws UnknownHostException {
        new Main();

    }
    class Product{
        int price;
        String name, mod;
        public Product(String name, String mod, int price){
            this.name = name;
            this.mod = mod;
            this.price = price;
        }
    }
}
