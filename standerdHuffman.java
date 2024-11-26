import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;


public class standerdHuffman implements Algo {
    private class Node{
        char data;
        int f;
        Node left;
        Node right;
        Node(char data, int f){
            this.data=data;
            this.f=f;
            left=right=null;
        }
    }
        String lineToCompression = "";
        HashMap<Character,String>result=new HashMap<>();
        Node root;
        StringBuilder resultString=new StringBuilder();
        ///////////////////////////
        public void comperssion() {
        Scanner scanner = new Scanner(System.in);
  
        try {
            System.out.println("what is the file name: ");
            String filename = scanner.nextLine();
            File myObj = new File(filename + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                lineToCompression = myReader.nextLine();
                System.out.println(lineToCompression);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            
        }
        HashMap<Character,Integer> freqmap=new HashMap<>();
        for(char c:lineToCompression.toCharArray()){
            freqmap.put(c,freqmap.getOrDefault(c, 0)+1);
        }
        PriorityQueue<Node>hTree=new PriorityQueue<>((x,y)-> x.f - y.f);
        for (char c : freqmap.keySet()) {
            hTree.add(new Node(c,freqmap.get(c)));
            
        }
        while(hTree.size()>1){
            Node left = hTree.poll();
            Node right =hTree.poll();
            Node totalnode=new Node('S', left.f+right.f);
            totalnode.left=left;
            totalnode.right=right;
            hTree.add(totalnode);
        }
        root=hTree.poll();
        print(root,new StringBuilder());
        Huffmanprint();


        }
        void print(Node root,StringBuilder code){
            if(root==null)return;
            if(root.data!='S'){
                result.put(root.data,code.toString());
                    // System.out.println(root.data + ": " + code);
            }
            if(root.left!=null){
                print(root.left,code.append('0'));
                code.deleteCharAt(code.length()-1);
            }
            if(root.right!=null){
                print(root.right,code.append('1'));
                code.deleteCharAt(code.length()-1);
            }}
            void Huffmanprint(){
                if(result==null||result.isEmpty()){
                    System.out.println("opps!");
                }
            for(char c:lineToCompression.toCharArray()){
                resultString.append(result.get(c));
            }
            // System.out.println(resultString);
            try {
            FileWriter myWriter = new FileWriter("filename.txt");
            for (int i = 0; i < resultString.length(); i++) {
                myWriter.write((int)resultString.charAt(i));}
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

        @Override
        public void decompresion() {
            Node curr=root;
            StringBuilder decomp=new StringBuilder();

            for(int x:resultString.toString().toCharArray()){
                if(x=='0'){
                    curr=curr.left;
                }
                else if (x=='1'){
                    curr=curr.right;
                }
                if(curr.left==null && curr.right==null){
                    decomp.append(curr.data);
                    curr=root;
                }
            }System.out.println(decomp);
            try {
                FileWriter myWriter = new FileWriter("decomp.txt");
                    myWriter.write(decomp.toString());
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        }
}
