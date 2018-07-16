package r;

public class Edge {
    public String source;
    public String target;

    public double weight=1;

    public Edge(String source, String target){
        this.source=source;
        this.target=target;
    }


    public Edge(String source, String target,double weight){
        this(source,target);
        this.weight=weight;
    }

}
