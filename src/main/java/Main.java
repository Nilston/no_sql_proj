import com.mongodb.*;
import com.mongodb.util.JSON;
import org.bson.BasicBSONObject;

import java.io.BufferedReader;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.sun.tools.doclint.Entity.and;
import static com.sun.tools.doclint.Entity.prod;
import static javax.swing.text.StyleConstants.Size;

/**
 * Created by anton on 2017-05-22.
 */
public class Main  {
    List <Product>productList = new ArrayList();
    int totalprice = 0;
    String ssnnbr = "";
    Date date = new Date();
    MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    DB database = mongoClient.getDB("BeaverCoffee");
    Scanner scan = new Scanner(System.in);
    public Main() throws UnknownHostException {

        System.out.println("Welcome to BeaverCoffee, do you want to create the demo database? (only do it once)\n1. Yes, set it up!\n2. No, I already have it");
        int in = scan.nextInt();
        switch (in) {
            case 1: setUpDatabase();
                System.out.print("\n\nThe database has been set up!");
                break;
            default: System.out.print("\nNothing changed!");
                break;
        }

        System.out.print("\n-----------------------------------\nWhat would you like to do?\n");
        boolean temp = true;
        while (temp) {
            if (ssnnbr.length() != 10) {
                System.out.println("Enter your SSN(10 digits) to use this machine. (Default sign in 0000000000) ");
                ssnnbr = scan.next();
                DBCollection employee = database.getCollection("Employee");
                BasicDBObject query = new BasicDBObject();
                query.put("SSN", ssnnbr);
                DBCursor cursor = employee.find(query);
                if (cursor.hasNext()) {
                    temp = false;
                } else {
                    System.out.println("Wrong login! If you cant remember yours, use default!");
                }
            }
        }
        while (true) {
                System.out.println();
                System.out.println("1. Place an order!\n2. Update an order!\n3. Delete an order!\n4. Add member!\n5. Employees\n6. Stock\n7. Get sales\n8. Who sold what?");
                int input = scan.nextInt();
                switch (input) {
                    case 1:
                        placeOrder();
                        break;
                    case 2:
                        updateOrder();
                        break;
                    case 3:
                        deleteOrder();
                        break;
                    case 4:
                        addMember();
                        break;
                    case 5:
                        employees();
                        break;
                    case 6:
                        stock();
                        break;
                    case 7:
                        getSales();
                        break;
                    case 8:
                        whoSoldThat();
                        break;
                    default:
                        System.out.print("No, wrong");
                        break;
                }
            }
    }

    private void whoSoldThat() {
        System.out.println("Specify the starting time period like: yyyy-MM-dd_hh:mm:ss");
        String start = scan.next();

        System.out.println("Specify the ending time period like: yyyy-MM-dd_hh:mm:ss");
        String end = scan.next();
        Date fromDate = new Date();
        Date toDate = new Date();
        try {
            fromDate = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss").parse(start);
            toDate = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss").parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("Please give me the SSN of the employee: ");
        String ssn = scan.next();

        DBCollection collection = database.getCollection("Order");
        BasicDBObject query = new BasicDBObject();
        query.put("date", BasicDBObjectBuilder.start("$gte", fromDate).add("$lte", toDate).get());
        BasicDBObject query2 = new BasicDBObject("serving_employee", ssn);

        BasicDBList and = new BasicDBList();
        and.add(query);
        and.add(query2);

        BasicDBObject q = new BasicDBObject("$and", and);
        DBCursor cursor = collection.find(q).sort(new BasicDBObject("date", -1));
        System.out.println("This is the orders performed from " + fromDate + " to " + toDate);
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            System.out.println(obj.get("_id"));
        }
    }

    private void getSales() {
        System.out.println("Specify the starting time period like: yyyy-MM-dd_hh:mm:ss");
        String start = scan.next();

        System.out.println("Specify the ending time period like: yyyy-MM-dd_hh:mm:ss");
        String end = scan.next();
        Date fromDate = new Date();
        Date toDate = new Date();
        try {
            fromDate = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss").parse(start);
            toDate = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss").parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DBCollection collection = database.getCollection("Order");
        BasicDBObject query = new BasicDBObject();
        query.put("date", BasicDBObjectBuilder.start("$gte", fromDate).add("$lte", toDate).get());
        DBCursor cursor = collection.find(query).sort(new BasicDBObject("date", -1));
        System.out.println("This is the orders performed from " + fromDate + " to " + toDate);
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            System.out.println(obj.get("_id"));
        }
    }

    private void stock() {
        DBCollection location = database.getCollection("Location");
        DBCursor cursor = location.find();

        while (cursor.hasNext()) {
            DBObject products = cursor.next();
            DBObject prods = (DBObject) products.get("products");
            Map map = prods.toMap();
            Object []arr = map.values().toArray();
            for(Object o : arr) {
                System.out.println(o + "\n");
            }
        }
    }

    private void employees() {
        DBCollection employees = database.getCollection("Employee");
        DBCollection location = database.getCollection("Location");
        DBCursor cursor = employees.find();
        BasicDBObject employee;
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to add(1) or see(2) current employees?\n Or do you as an employer wanna leave a comment" +
                "about an employee?(3)");
        int option = input.nextInt();
        if(option == 1){
            System.out.println("Enter Employees SSN number");
            String ssn = input.next();
            System.out.println("Enter Employees full name");
            String name = input.next();
            System.out.println("Enter Employees start date");
            String startDate = input.next();
            System.out.println("Enter Employees form of employment");
            String formOfEmployment = input.next();
            employee = new BasicDBObject("SSN", ssn).append("name", name).append("start_date", startDate).append("form_of_employment", formOfEmployment);
            employees.insert(employee);
        }else if(option == 2) {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } else if(option == 3){
            System.out.println("Enter your SSN to validate your authority");
            String employerSsn = input.next();
            DBCursor cursor1 = location.find();
            BasicDBObject privileges = (BasicDBObject) cursor1.next().get("employer");
            BasicDBList commentList = new BasicDBList();

            if (privileges.get("SSN").equals(employerSsn)){
                System.out.println("Enter the employees SSN number.");
                String employeeSsn = input.next();
                DBCursor cursor2 = employees.find();
                while (cursor2.hasNext()){
                    if(cursor2.next().get("SSN").equals(employeeSsn)) {
                        System.out.println("Enter a comment for " + employeeSsn);
                        String apa = input.nextLine();
                        String comment = input.nextLine();

                        if(comment.length() > 300){
                            comment = comment.substring(0, 300);
                        }
                        commentList.add(new BasicDBObject("Employer Id", employerSsn).append("date_of_entry", date).append("comment", comment));
                        BasicDBObject newDocument = new BasicDBObject();
                        newDocument.append("$set", new BasicDBObject().append("comment", commentList));
                        BasicDBObject searchQuery = new BasicDBObject().append("SSN", employeeSsn);
                        employees.update(searchQuery, newDocument);
                    }
                    System.out.println();
                }
            }
        }
        System.out.println();
    }

    private void addMember() {
        Scanner input = new Scanner(System.in);
        DBCollection members = database.getCollection("Member");
        BasicDBObject member;
        BasicDBList addressList = new BasicDBList();
        DBCursor cursor = members.find();
        System.out.println("Would you like to add(1) or see(2) current members?");
        int option = input.nextInt();
        if(option == 1){
            System.out.println("Enter Members SSN number(10 digits)");
            String ssn = input.next();
            if(ssn.length() < 10){
                System.out.println("Enter Members SSN number(10 digits)");
                ssn = input.next();
            }
            System.out.println("Enter Members occupation");
            String occupation = input.next();
            System.out.println("Enter Members barcode number");
            String barcode = input.next();
            System.out.println("Enter Members Street address");
            String apa = input.nextLine();
            String street = input.nextLine();
            System.out.println("Enter Members City of residens");
            String city = input.next();

            addressList.add(new BasicDBObject("street", street).append("city", city));
            member = new BasicDBObject("SSN", ssn).append("occupation", occupation).append("barcode", barcode).append("address", addressList);
            members.insert(member);
        }else if(option == 2) {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        }
        System.out.println();
    }

    private void deleteOrder() {
        System.out.print("Please type the ID of the order to delete:");
        int orderID = scan.nextInt();

        DBCollection collection = database.getCollection("Order");
        BasicDBObject query = new BasicDBObject();
        query.append("_id", orderID);
        collection.remove(query);
    }

    /**
     * Updates an existing order. You can't add och delete products to the product list in
     * an order, only edit existing products. VERY simplistic!
     */
    private void updateOrder() {
        System.out.print("Please tell me the ID of the order number");
        int ordernumber = scan.nextInt();

        DBCollection collection = database.getCollection("Order");
        DBObject query = new BasicDBObject("_id", ordernumber);
        DBCursor cursor = collection.find(query);
        DBObject order = cursor.one();

        BasicDBObject newOrder = new BasicDBObject();
        System.out.print("What do you want to do?\n1. Edit product list\n2. Edit currency");
        int in = scan.nextInt();
        switch (in) {
            case 1:
                System.out.println("Existing products in the list:");

                BasicDBList l = (BasicDBList) order.get("productlist");
                for (int i = 0; i < l.size(); i++) {
                    BasicDBObject product = (BasicDBObject) l.get(i);
                    System.out.println("" + i + ". " + product.get("name"));
                }
                System.out.println("Select what product to edit:");
                int product = scan.nextInt();
                l.remove(product);
                System.out.println("Type the product you want!");
                String line = scan.next();
                System.out.println("Any addons?");
                String line2 = scan.next();
                System.out.println("what is the price?");
                String line3 = scan.next();
                l.add(new BasicDBObject("name", line).append( "mod", line2).append("price", line3));
                newOrder.append("$set", new BasicDBObject().append("productlist", l));
                break;
            case 2:
                System.out.print("Please type the currency used: ");
                String curr = scan.nextLine();
                newOrder.append("$set", new BasicDBObject().append("currency", curr));
                break;
            default:
                System.out.print("Nothing chosen, aborting editing\n");
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
            System.out.println("Do you want anything more?\n1. Yes!\n2. No!");
            int input3 = scan.nextInt();
            if(input3 == 2){
                DBCollection order = database.getCollection("Order");
                BasicDBObject newOrder = new BasicDBObject();
                BasicDBList proList = new BasicDBList();
                int milkcount =  0, coffeecount = 0, choclatecount = 0;
                for(int j = 0; j < productList.size(); j++){
                    proList.add(new BasicDBObject("name", productList.get(j).getName()).append( "mod", productList.get(j).getMod()).append("price", productList.get(j).getPrice()));
                    if(productList.get(j).getName().equals("Hot Chocolate (Cocoa Mix)")){
                        choclatecount ++;
                        milkcount++;
                    }else if(productList.get(j).getName().equals("Latte") || productList.get(j).getName().equals("Capuccino") ){
                        milkcount ++;
                        coffeecount++;
                    }else{
                        coffeecount ++;
                    }
                }
                try {
                    newOrder.put("_id", getNextSequence("orderid"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateQuantity("choclate mix", choclatecount);
                updateQuantity("coffee", coffeecount);
                updateQuantity("milk", milkcount);

                newOrder.put("productlist", proList);
                newOrder.put("currency", "sek");
                newOrder.put("serving_employee", ssnnbr);
                newOrder.put("date", date);
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

    public void updateQuantity(String content, int amount){
        DBCollection location = database.getCollection("Location");
        BasicDBObject query = new BasicDBObject();
        query.put("name", "BeaverCoffee Malmö");
        query.append("products.name", content);
        BasicDBObject update = new BasicDBObject().append("$inc", new BasicDBObject().append("products.$.quantity", - amount));
        location.update(query, update);
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
        syrupCheck(product);
        productList.add(product);
        totalprice += product.price;
        System.out.println("Added an " + product.name + ", " + product.mod + " that costed " + product.price + " kr.");
    }
    public void syrupCheck(Product p){
        System.out.println("Do you want any syrup for that one?\n1. Vanilla!\n2. Caramel!\n3. Irish Cream!\n" +
                "4. No syrup");
        int input4 = scan.nextInt();
        switch (input4) {
            case 1:
                p.mod = p.mod + " with Vanilla Syrup";
                break;
            case 2:
                p.mod = p.mod + " with Caramel Syrup";
                break;
            case 3:
                p.mod = p.mod + " with Irish Cream Syrup";
                break;
            case 4:
                break;
        }
    }
    public void setUpDatabase() {
        DBCollection location = database.getCollection("Location");
        BasicDBObject locationObj = new BasicDBObject();
        BasicDBList productList1 = new BasicDBList();

        BasicDBObject manager = new BasicDBObject("SSN", "6403114120")
                .append("access_control", "unlimited")
                .append("name", "Nisse Dahlgren")
                .append("start_date", "2000-01-01")
                .append("end_date", "2003-09-11");

        BasicDBObject employer = new BasicDBObject("SSN", "6403114120")
                .append("access_control", "unlimited")
                .append("name", "Kalle Blomkvist")
                .append("start_date", "2000-01-01")
                .append("end_date", "2003-09-11");

        productList1.add(new BasicDBObject("name", "coffee").append("quantity", 10000));
        productList1.add(new BasicDBObject("name", "milk").append("quantity", 10000));
        productList1.add(new BasicDBObject("name", "choclate mix").append("quantity", 5000));

        locationObj.put("name", "BeaverCoffee Malmö");
        locationObj.put("manager", manager);
        locationObj.put("employer", employer);
        locationObj.put("products", productList1);

        location.insert(locationObj);
        System.out.println("Location added.");

        DBCollection counters = database.getCollection("counters");
        BasicDBObject counter = new BasicDBObject("_id", "orderid").append("seq", 0);
        counters.insert(counter);
        System.out.println("Counters added!");

        DBCollection employees = database.getCollection("Employee");
        BasicDBObject employeesObj = new BasicDBObject();
        employeesObj.put("SSN", "0000000000");
        employeesObj.put("name", "Jöns Persson");
        employeesObj.put("start_date", "2001-02-24");
        employeesObj.put("form_of_employment", "Assistant Manager");

        BasicDBObject employeesObj2 = new BasicDBObject();
        employeesObj2.put("SSN", "1709290501");
        employeesObj2.put("name", "Per Jönssson");
        employeesObj2.put("start_date", "2011-02-25");
        employeesObj2.put("form_of_employment", "Janitor");

        employees.insert(employeesObj);
        employees.insert(employeesObj2);

        System.out.println("2 Employees added.");

        DBCollection member = database.getCollection("Member");
        BasicDBObject membersObj = new BasicDBObject();
        BasicDBList adressList = new BasicDBList();

        adressList.add(new BasicDBObject("street", "Ronnevägen 11")
                .append("city", "Köln"));

        membersObj.put("SSN", "6412123454");
        membersObj.put("occupation", "Dancer");
        membersObj.put("barcode", "iasd99998aaasd");
        membersObj.put("address", adressList);

        member.insert(membersObj);

        System.out.println("1 Member added.");

        DBCollection order = database.getCollection("Order");
        BasicDBObject orderObj = new BasicDBObject();
        BasicDBList productList2 = new BasicDBList();
        productList2.add(new BasicDBObject("name", "Brewed Coffee")
                .append("mod", " with Vanilla Syrup")
                .append("price", 20));
        productList2.add(new BasicDBObject("name", "Espresso")
                .append("mod", " with Caramel Syrup")
                .append("price", 20));
        productList2.add(new BasicDBObject("name", "Latte")
                .append("mod", "2% Milk")
                .append("price", 30));

        try {
            orderObj.put("_id", getNextSequence("orderid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderObj.put("productlist", productList2);
        orderObj.put("serving_employee", "0000000000");
        orderObj.put("date", date);
        orderObj.put("currency", "sek");

        BasicDBObject orderObj2 = new BasicDBObject();
        BasicDBList productList3 = new BasicDBList();
        productList3.add(new BasicDBObject("name", "Latte")
                .append("mod", ",2% Milk with Vanilla Syrup")
                .append("price", 20));
        productList3.add(new BasicDBObject("name", "Espresso")
                .append("mod", " with Caramel Syrup")
                .append("price", 20));
        productList3.add(new BasicDBObject("name", "Espresso")
                .append("mod", "")
                .append("price", 30));

        try {
            orderObj2.put("_id", getNextSequence("orderid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderObj2.put("productlist", productList3);
        orderObj2.put("serving_employee", "28091234");
        orderObj2.put("date", date);
        orderObj2.put("currency", "dollar");


        order.insert(orderObj);
        order.insert(orderObj2);
        System.out.println("2 Orders added.");
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

    public Object getNextSequence(String name) throws Exception {
        DBCollection collection = database.getCollection("counters");
        BasicDBObject find = new BasicDBObject();
        find.put("_id", name);
        BasicDBObject update = new BasicDBObject();
        update.put("$inc", new BasicDBObject("seq", 1));
        DBObject obj = collection.findAndModify(find, update);
        return obj.get("seq");
    }
}
