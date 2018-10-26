package mikenikitin.plagiat2.services;

public class Combiner {

    int amount=0;
    int [][] comb;
    String[] words;

    int[] swap(int in[],int a,int b){
        int[]out=in.clone();
        out[b]=in[a];out[a]=in[b];
        return out;
    }

    String out(int[] a){
        String s="";
        for (int i=0;i<a.length;i++ )
            s = s+words[a[i]]+' ';
        return s;
    }

    int combiner(int n){
        if (n>2) {
            int nf=combiner(n-1);
            for(int i=0;i<nf;i++)
                for(int j=1;j<n;j++)
                    comb[nf*j+i]=swap(comb[nf*(j-1)+i],n-j,n-j-1);
            return nf*n;
        }
        // N=2
        comb[1]=swap(comb[0],0,1);
        return 2;
    }

    public Combiner(String str){
        words = str.split(" ");
        comb=new int[factorial(words.length)][words.length];
        for (int i=0;i<words.length;i++)comb[0][i]=i;
        amount=combiner(words.length);
    }

    public String randomOut(){return out(comb[(int)(Math.random()*amount)]);}

    int factorial(int n) {
        if (n>0) return factorial(n-1)*n;
        return 1;
    }

    void fullOut()
    { // System.out.println(comb.length);
        for(int i=0;i<comb.length;i++)
            System.out.println(i+" : "+out(comb[i]));
    }

}
