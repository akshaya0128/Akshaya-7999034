interface Playable {
    void play();
}

class Guitar implements Playable {
    @Override
    public void play() {
        System.out.println("Playing the Guitar: Strum strum strum!");
    }
}

class Piano implements Playable {
    @Override
    public void play() {
        System.out.println("Playing the Piano: Tinkle tinkle tinkle!");
    }
}

public class InterfaceExample {
    public static void main(String[] args) {
        Playable guitar = new Guitar();
        Playable piano = new Piano();

        guitar.play();
        piano.play();
    }
}
