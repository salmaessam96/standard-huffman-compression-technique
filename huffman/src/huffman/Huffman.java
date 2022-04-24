package huffman;
import java.util.*;
import java.io.*;
public class Huffman {
   static String line,decoded;
   static int size,s,m,compsize;
   ArrayList <Letter> letters=new ArrayList<Letter>();
   ArrayList <Node> nodes=new ArrayList<Node>();
class Letter{
    char letter;
    int frequency;
    String codes;
    
    Letter(){}
    Letter(char letter,int frequency){
        this.letter=letter;
        this.frequency=frequency;
    }
}
public class Node{
    Node left=null,
    right=null;
    char character;
    int frequent;
    String code;
    Node(){
	character = '.';
	frequent = 0;
	code = "";
        left=null;
        right=null;
    }
    Node(char c,int f){
        character=c;
        frequent=f;
    }
    Node(char c,int f,String co,Node l,Node r){
        character=c;
        frequent=f;
        code=co;
        left=l;
        right=r;
    }
    public void replace(){
        s=size;
        for(int i=0;i<size;i++){
            Node tmp=new Node(letters.get(i).letter,letters.get(i).frequency,"",null,null);
            nodes.add(tmp);
        }
        arrange();
        min();
    }
    public void arrange(){
        while(nodes.size()!=1){
            Node parent=new Node('/',nodes.get(0).frequent+nodes.get(1).frequent);
            parent.left=nodes.get(0);
            parent.right=nodes.get(1);
            nodes.add(parent);
            nodes.remove(0);
            nodes.remove(0);
            min();
        } 
       letters.clear();
       contain("dictionary: "); 
       System.out.println("dictionary:");
       printCode(nodes.get(0),"");
       encode();
       diff();
    }
    public void min(){
        Node temp=new Node();
        for (int i = 1; i < nodes.size(); i++) {
            for (int j = i; j > 0; j--) {
                if (nodes.get(j).frequent< nodes.get(j-1).frequent) {
                    temp.character= nodes.get(j).character;
                    temp.frequent= nodes.get(j).frequent;
                    temp.left= nodes.get(j).left;
                    temp.right= nodes.get(j).right;
                    nodes.get(j).character= nodes.get(j-1).character;
                    nodes.get(j).frequent= nodes.get(j-1).frequent;
                    nodes.get(j).left= nodes.get(j-1).left;
                    nodes.get(j).right= nodes.get(j-1).right;
                    nodes.get(j-1).character= temp.character;
                    nodes.get(j-1).frequent= temp.frequent;
                    nodes.get(j-1).left= temp.left;
                    nodes.get(j-1).right= temp.right;
                }
            }
        }
    }
    public void traverse(Node n){
        if (n != null) {
            if(n.left==null&&n.right==null)
                System.out.println(" " + n.character);
            traverse(n.left);
            traverse(n.right);
        } 
    }
    
    public void printCode(Node r, String b) {
    if (r.left == null && r.right == null) {    
        r.code=b;
        Letter l=new Letter(r.character,r.frequent);
        l.codes=r.code;
        System.out.println(r.character + "   |  " + b);
        contain(r.character+" "+b); 
        letters.add(l);
        return;
    }
    else{
        printCode(r.left, b + "0");
        printCode(r.right, b + "1");
    }
  }
}
public void diff(){
    int l,f,sum;
    compsize=0;
    for(int i=0;i<letters.size();i++){
        l=0;
        f=0;
        sum=0;
        l=letters.get(i).codes.length();
        f=letters.get(i).frequency;
        sum=l*f;
        compsize=compsize+sum;
    }
    int originalsize=line.length()*8;
    int dif=originalsize-compsize;
    System.out.println("\noriginal size: "+line.length()+"*8= "+originalsize+" bits.\ncompressed size: "+compsize+" bits.\ndifference: "+
    originalsize+" - "+compsize+"= "+dif+" bits.");
}
public void search(char key)
{
    String compressed="",m="";
    int compare=0;
    for(int i=0;i<letters.size();i++){
        compare = Character.compare(letters.get(i).letter, key);
        if(compare==0){
            m=letters.get(i).codes;
            compressed=compressed+m;
            System.out.print(compressed);
            containcomp(compressed);
        }
    }
}
public void encode(){
    System.out.print("the compressed string:  ");
    char[] ch=line.toCharArray();
    for(int i=0;i<line.length();i++){
        search(ch[i]);
    }
}
public String extract(){
    try {
        FileReader reader = new FileReader("text.txt");
        BufferedReader bufferedReader = new BufferedReader(reader);
        line = bufferedReader.readLine();       
        reader.close();
    }catch (IOException e) {
        e.printStackTrace();
    }
    return line;
}
public void contain(String l){
try {
    FileWriter writer = new FileWriter("dictionary.txt", true);
    BufferedWriter bufferedWriter = new BufferedWriter(writer);
    bufferedWriter.write(l);
    bufferedWriter.newLine();
    bufferedWriter.close();
}catch (IOException e) {
    e.printStackTrace();
}
}
public void containcomp(String l){
try {
    m=l.length()+m;
    FileWriter writer = new FileWriter("compressedline.txt", true);
    BufferedWriter bufferedWriter = new BufferedWriter(writer);
    bufferedWriter.write(l);
    bufferedWriter.close();
}catch (IOException e) {
    e.printStackTrace();
}
}
public void freq(){
    extract();
    float len= line.length();
    Letter l;
    char[] ch=line.toCharArray();  
    for(int i=0;i<len;i++){
        int counter=0;
        for(int j=0;j<len;j++){
            if(ch[j]==ch[i]){
                counter++;
            }
        }
        l = new Letter(ch[i],counter);
        letters.add(l);
    }
    duplicate();
}
public void duplicate(){
    size=line.length();
    char temp,temp2;
    for(int i=0;i<size;i++){
        temp=letters.get(i).letter;
        for(int j=0;j<size;j++){
            temp2=letters.get(j).letter;
            if(i!=j){
                if(temp2==temp){
                    letters.remove(j);
                    j--;
                    size--;
                }
            }
        }
    }
    Node n=new Node();
    n.replace();
}
public void decompress(){
    extractde();
    extractdic();
    String c;
    int l;
    for(int i=1;i<letters.size();i++){
        l=letters.get(i).codes.length();
        letters.get(i).letter=letters.get(i).codes.charAt(0);
        c=letters.get(i).codes.substring(2,l);
        letters.get(i).codes="";
        letters.get(i).codes=c;
    }
    searching();
    System.out.println("\ndecompressed string:  "+decoded);
    containdecoded(decoded);
}
public String extractde(){
    try {
        FileReader reader = new FileReader("compressedline.txt");
        BufferedReader bufferedReader = new BufferedReader(reader);
        line = bufferedReader.readLine();       
        reader.close();
    }catch (IOException e) {
        e.printStackTrace();
    }
    return line;
}
public void extractdic(){
    try {
        letters.clear();
        File reader = new File("dictionary.txt");
        Scanner myReader = new Scanner(reader);
        while (myReader.hasNextLine()) {
            Letter t=new Letter();
            t.codes = myReader.nextLine();
            letters.add(t);
        }
        myReader.close();
    }catch (IOException e) {
        e.printStackTrace();
    }
}
public void searching(){
    String k="";
    decoded="";
    int start=0,end=1;
    for(int i=0;i<line.length();i++){
        k=k+line.charAt(i);
        for(int j=0;j<letters.size();j++){
            if (letters.get(j).codes.equals(k)){
                decoded=decoded+letters.get(j).letter;
                k="";
            }
        }
    }
}
public void containdecoded(String d){
    try {
        FileWriter writer = new FileWriter("decompressedline.txt", true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(d);
        bufferedWriter.newLine();
        bufferedWriter.close();
    }catch (IOException e) {
        e.printStackTrace();
    }
}
public static void main(String[] args) {
    Huffman f=new Huffman();
    f.freq();
    f.decompress();
}
}
