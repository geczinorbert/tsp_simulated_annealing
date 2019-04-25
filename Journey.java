import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Journey {

    private ArrayList<City> cities;

    public Journey(ArrayList<City> cities) {
        this.cities = cities;
    }

    public Journey() {
        cities = new ArrayList<>();
    }

    public void add(City city) {
        cities.add(city);
    }

    public int size() {
        return cities.size();
    }

    public City get(int i) {
        return cities.get(i);
    }

    @Override
    public String toString() {
        return "Journey " + cities;
    }

    public static double distance(City from, City to) {
        double x = Math.abs(from.getX() - to.getX());
        double y = Math.abs(from.getY() - to.getY());
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public void shuffle() {
        Collections.shuffle(cities);
    }

    @SuppressWarnings("unchecked")
    public Journey clone() {
        return new Journey((ArrayList<City>) cities.clone());
    }

    public void random_neighbor() {
        Random random = new Random();
        int i;
        int j;
        do {
            i = random.nextInt(size());
            j = random.nextInt(size());
        } while (i == j);
        City iCity = cities.get(i);
        City jCity = cities.get(j);
        cities.set(i, jCity);
        cities.set(j, iCity);
    }
}
