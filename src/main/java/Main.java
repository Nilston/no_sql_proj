import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by anton on 2017-05-22.
 */
public class Main {
    MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    Scanner scan = new Scanner(System.in);
    public Main() throws UnknownHostException {
        DB database = mongoClient.getDB("BeaverCoffee");
        DBCollection col = database.getCollection("Order");
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

    }

    private void addMember() {
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
