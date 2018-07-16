package r;

import graph.Graph;
import helpers.Options;
import helpers.StressCalculator;
import helpers.StressFactory;
import io.Pair;
import layout.PivMDS;
import layout.SparseStressModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphLayout {

    public static Options getDefaultOptions() {
        Options o = new Options();

        o.setNumOfPiv(800);
        o.setSampleStyle(StressFactory.SAMPLING_STYLE.KMEANS);
        o.setFactor(1);
        o.setNumOfIter(200);
        o.setBC(true);
        o.setNumPivotsMDS(800);
        o.setNumberOfKMeansSources(200);
        //-p 50|100|200 -s kmeans -f 1 -i 200 +b -m 200 -k 50|100|200

        return o;
    }


    public static double[] layout(List<String> nodes, List<Edge> edges) {

        Options options = getDefaultOptions();


        ArrayList<Pair>[] eList = new ArrayList[nodes.size()];

        HashMap<String, Integer> nodeId = new HashMap<>(nodes.size());

        int j = 0;
        for (String node : nodes) {
            if (nodeId.containsKey(node)) continue;
            nodeId.put(node, j);
            j++;
        }

        for (int i = 0; i < eList.length; i++)
            eList[i] = new ArrayList<Pair>();


        for (Edge e : edges) {
            Integer index1 = nodeId.get(e.source);
            Integer index2 = nodeId.get(e.target);
            double weight = 1;

            eList[index1].add(new Pair(index2, weight));
            eList[index2].add(new Pair(index1, weight));
        }


        Graph g = new Graph(eList, false);

        final long start = System.currentTimeMillis();
        // calculate pivot mds layout
        final double[] layout = new PivMDS().doLayout(g, options.getNumOfPivotsMDS());
        // calculate sparse stress layout
        new SparseStressModel().doLayout(g, layout, options);
        final double time = (System.currentTimeMillis() - start) / 1000d;

        System.out.println("time: " + time);

        return layout;
    }

    public static void main(String[] args){
        List<String> nodes=new ArrayList<String>();

        nodes.add("nodeA");
        nodes.add("nodeB");
        nodes.add("nodeC");
        nodes.add("nodeD");

        List<Edge> edges=new ArrayList<Edge>();

        edges.add(new Edge("nodeA","nodeB"));
        edges.add(new Edge("nodeB","nodeC"));
        edges.add(new Edge("nodeC","nodeA"));
        edges.add(new Edge("nodeD","nodeA"));

        double[] result = layout(nodes, edges);

        System.out.println(result);
    }

}
