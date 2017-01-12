import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            Cocktails c = new Cocktails();
            c.start();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}

class Cocktails {
    private String[] lstWaiterRecipes, lstOrders;
    private List<Cocktail> cocktails;
    private String currentOrder;

    public void start() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int _numTasks = Integer.parseInt(br.readLine());
        for(int t = 0; t < _numTasks; t++) {
            int _numWaiters = Integer.parseInt(br.readLine());
            lstWaiterRecipes = new String[_numWaiters];
            for(int w = 0; w < _numWaiters; w++) {
                lstWaiterRecipes[w] = br.readLine();
            }


            int _numOrders = Integer.parseInt(br.readLine());
            lstOrders = new String[_numOrders];
            for(int o = 0; o < _numOrders; o++) {
                lstOrders[o] = br.readLine();
            }

            cocktails = getCocktails();
            for(int i = 0; i < _numOrders; i++){
                currentOrder = lstOrders[i];
                // If any of the orders isn't in the cocktail list just skip the current order
                if(!cocktailsExist(currentOrder)) {
                    System.out.println(currentOrder + " onmogelijk");
                    continue;
                }
                boolean test = evaluateOrders(0, new boolean[_numWaiters]);
                System.out.println(currentOrder + " " + (test ? "mogelijk" : "onmogelijk"));
            }
        }
    }

    private boolean evaluateOrders(int orderCocktailNo, boolean[] waitersBusy) {

        // Are we still processing the current order? (Ex.: Order = ABC, orderCocktailNo = 1)
        if(orderCocktailNo < currentOrder.length()){
            // Get the current cocktail (Ex. c = B)
            Cocktail c = findCocktail(currentOrder.charAt(orderCocktailNo));
            if(c != null)
            // Check if ANY waiter can make this cocktail and is available
            for(int waiter : c.getWaiters()) {
                if(waitersBusy[waiter] == false) {
                    // Set the available waiter to busy
                    waitersBusy[waiter] = true;
                    // Do the next cocktail
                    boolean next = evaluateOrders(orderCocktailNo + 1, waitersBusy);
                    // If the next cocktail was made successfully, return true, else do backtracking
                    if(!next) {
                        waitersBusy[waiter] = false;
                        continue;
                    }
                    return next;
                }
            }
            // No waiter was found who is able to do the job
            return false;
        }
        // The order has been completed
        return true;
    }

    private List<Cocktail> getCocktails() {
        List<Cocktail> _cocktails = new ArrayList<>();
        for(int i = 0; i < lstWaiterRecipes.length; i++) {
            for(int j = 0; j < lstWaiterRecipes[i].length(); j++) {
                Cocktail cocktail = new Cocktail(lstWaiterRecipes[i].charAt(j));
                if(!_cocktails.contains(cocktail)) {
                    for(int k = 0; k < lstWaiterRecipes.length; k++) {
                        String cocktailName = "" + lstWaiterRecipes[i].charAt(j);
                        if(lstWaiterRecipes[k].contains(cocktailName)) cocktail.addWaiter(k);
                    }
                    _cocktails.add(cocktail);
                }
            }
        }
        return _cocktails;
    }

    private Cocktail findCocktail(char cocktailName) {
        for(Cocktail c : cocktails){
            if(c.getCocktailName() == cocktailName) return c;
        }
        return null;
    }

    private boolean cocktailsExist(String order){
        for(int i = 0; i < order.length(); i++){
            if(!cocktails.contains(findCocktail(order.charAt(i)))) return false;
        }
        return true;
    }

}

class Cocktail {
    private char _cockTailName;
    private List<Integer> _waiters;

    public Cocktail(char cocktailName) {
        this._cockTailName = cocktailName;
        this._waiters = new ArrayList<>();
    }

    public void addWaiter(int cocktail) {
        this._waiters.add(cocktail);
    }

    public char getCocktailName(){
        return _cockTailName;
    }

    public List<Integer> getWaiters(){
        return _waiters;
    }

    public boolean equals(Object object) {
        if(object instanceof Cocktail && ((Cocktail)object).getCocktailName() == this._cockTailName) {
            return true;
        } else {
            return false;
        }
    }
}
