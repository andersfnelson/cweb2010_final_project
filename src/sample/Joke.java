package sample;

public class Joke {
    String joke;
    String punchline;

    public Joke (String  joke){
        this.joke = joke;
    }
    public Joke(String joke, String punchline){
        this.joke = joke;
        this.punchline = punchline;
    }
    public String getJoke(){

        return this.joke;
    }

    @Override
    public String toString(){
        return joke;
    }
}