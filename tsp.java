import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class tsp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n = 0;
        String filename = "";
        if (args.length < 2) {
            System.out.println("Too few arguments!");
            System.exit(1);
        }
        if (args.length > 2) {
            System.out.println("Too much arguments!");
            System.exit(1);
        }
        if (args.length == 2) {
            if (args[0].equals("-r")) {
                try {
                    n = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number!");
                    System.exit(1);
                }
            } else if (args[0].equals("-f")) {
                filename = args[1];
                try {
                    FileReader fileReader = new FileReader(filename);
                } catch (FileNotFoundException ex) {
                    System.out.println("Unable to open file '" + filename + "'");
                    System.exit(1);
                }
            } else {
                System.out.println("Invalid command!");
                System.exit(1);
            }
        }
        if (filename.equals("")) {
            Journey act = new Journey();
            act = createRandomJourney(n);
            simulated_annealing(act);
        } else {
            Journey act = new Journey();
            act = createFromFile(filename);
            simulated_annealing(act);
        }
    }


    public static Journey createFromFile(String fileName) {
        Journey act = new Journey();
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (tokens.length != 3) {
                    throw new IllegalArgumentException();
                }
                String name = tokens[0];
                String x_coordinate = tokens[1];
                String y_coordinate = tokens[2];
                act.add(new City(name, Integer.parseInt(x_coordinate), Integer.parseInt(y_coordinate)));
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        return act;
    }

    public static Journey createRandomJourney(int n) {
        Journey act = new Journey();
        int min = -1;
        int max = 1;
        for (int i = 0; i < n; ++i) {
            double xValue = min + Math.random() * (max - min);
            double yValue = min + Math.random() * (max - min);
            if (xValue < 0) {
                xValue = -xValue;
            }
            if (yValue < 0) {
                yValue = -yValue;
            }
            //System.out.println("(" + (int)(xValue*100) + "," + (int)(yValue*100) + ")");
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(i);
            String strI = sb.toString();
            act.add(new City(strI, (int) (xValue * 100), (int) (yValue * 100)));
        }
        //act.add(new City("Roma", 10, 50));
        return act;
    }

    public static void simulated_annealing(Journey act) {
        double temperature = 1.0;
        double alpha = 0.9;
        int iterations = 0;
        double threshold = 1;
        threshold = threshold / Math.pow(10, 308);
        act.shuffle();
        double bestTotalDistance = getTotalDistance(act);
        double newTotalDistance;
        while (temperature > threshold) {
            Journey next = act.clone();
            next.random_neighbor();
            newTotalDistance = getTotalDistance(next);
            if (newTotalDistance < bestTotalDistance || probability(bestTotalDistance, newTotalDistance, temperature)) {
                bestTotalDistance = newTotalDistance;
                act = next;
            }
            temperature *= alpha;
            iterations++;
        }
        System.out.println(act.toString());
        System.out.println("Distance " + bestTotalDistance);
        //System.out.println("Iterations" + iterations);
    }

    public static boolean probability(double bestTotalDistance, double newTotalDistance, double temperature) {
        return (Math.exp((bestTotalDistance - newTotalDistance) / temperature) > (double) new Random().nextInt(1000) / 1000);
    }

    public static double getTotalDistance(Journey act) {
        double totalDistance = 0.0;
        for (int i = 0; i < act.size(); i++) {
            if (i + 1 < act.size()) {
                totalDistance += Journey.distance(act.get(i), act.get(i + 1));
            } else {
                totalDistance += Journey.distance(act.get(i), act.get(0));
            }
        }
        return totalDistance;
    }
}
