import com.mongodb.*;
import com.mongodb.util.JSON;

import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Set;

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

    }

    private void placeOrder() {
        boolean theresMore = true;
        System.out.print("What do you like to order?");
        while(theresMore){
            
        }
    }



    public static void main(String args[]) throws UnknownHostException {
        new Main();

    }
}
