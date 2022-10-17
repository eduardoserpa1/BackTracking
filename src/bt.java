import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class bt {

    static ArrayList<String> testes = new ArrayList<>();
    static ArrayList<String> coordenadas_pelo_tamanho = new ArrayList<>();
    static HashMap<String, ArrayList<String>> mem = new HashMap<>();

    public static void main(String[] args) {
        switch (args.length){
            case 1:
                if(args[0].equals("test"))
                    run_tests();
                break;
            case 3:

                int n = Integer.parseInt(args[0]);
                int p = Integer.parseInt(args[1]);
                int g = Integer.parseInt(args[2]);

                long tempoInicial = System.currentTimeMillis();

                System.out.println(exec(n,p,g));

                long tempoFinal = System.currentTimeMillis();

                System.out.printf("%.3f ms", (tempoFinal - tempoInicial) / 1000d);

                break;
            default:
                break;
        }
    }

    static void load_tests(String path) throws IOException {
        BufferedReader buffRead = new BufferedReader(new FileReader(path));
        String linha = "";
        while(true){
            linha = buffRead.readLine();
            if(linha != null)
                testes.add(linha);
            else
                break;
        }
        buffRead.close();
    }

    static void run_tests(){
        System.out.println("Loading Tests");

        try{
            load_tests("./tests2.txt");
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Running Tests");

        for (int i = 0; i < testes.size()-1; i+=2){

            String[] split = testes.get(i).split(" ");

            int n = Integer.parseInt(split[0]);
            int p = Integer.parseInt(split[1]);
            int g = Integer.parseInt(split[2]);
            long result = Long.parseLong(testes.get(i+1));

            long tempoInicial = System.currentTimeMillis();

            boolean passed = unit_test(n, p, g,result);

            long tempoFinal = System.currentTimeMillis();

            System.out.println(passed+"  | "+String.format("%.3f ms", (tempoFinal - tempoInicial) / 1000d)+" |  Input[ "+n+", "+p+", "+g+", "+result+" ]");

        }
    }

    static boolean unit_test(int n, int p, int g, long result){
        long r = exec(n, p, g);
        if(r != result)
            System.out.println("OUT:: "+r);
        return r == result;
    }

    static long exec(int n, int p, int g){
        coordenadas_pelo_tamanho = retorna_coordenadas_da_matriz(n);
        mem = new HashMap<>();

        int pp = Math.min(p,g);
        int gg = Math.max(p,g);

        return combinatoria_porquinhos(clear_str_v(new String[n*n]),0, pp, gg);
    }

    static long retira_possibilidades_calcula_combinatoria(String[] v, int tamanho, int qtd_galinhas){
        ArrayList<String> coordenadas = new ArrayList<>();

        coordenadas.addAll(coordenadas_pelo_tamanho);

        int espacos_livres = filtra_coordenada(v,coordenadas,tamanho);

        if(qtd_galinhas < espacos_livres)
            return comb(BigInteger.valueOf(coordenadas.size()),BigInteger.valueOf(qtd_galinhas));
            //return combinatoria_galinhas(clear_str_v(new String[coordenadas.size()]),0,qtd_galinhas);
        else
        if(qtd_galinhas == espacos_livres)
            return 1;
        else
            return 0;
    }

    static long combinatoria_porquinhos(String[] v, int atual, int porquinhos, int galinhas){
        long r = 0;

        if(porquinhos == 0){
            return retira_possibilidades_calcula_combinatoria(v,(int)Math.sqrt(v.length),galinhas);
        }


        for (int i = atual; i < v.length; i++) {
            if(v[i].equals("p"))
                continue;
            v[i] = "p";
            r += combinatoria_porquinhos(v,i+1,porquinhos-1,galinhas);
            v[i] = " ";
        }

        return r;
    }

    static int filtra_coordenada(String[] v, ArrayList<String> coordenadas, int tamanho){
        ArrayList<String> posicoes_ocupadas = new ArrayList<>();

        for (int i = 0; i < v.length; i++)
            if(v[i] != " ")
                posicoes_ocupadas.add(coordenadas.get(i));

        ArrayList<String> coordenadas_para_remover;
        ArrayList<ArrayList<String>> lista_arrays_para_remover = new ArrayList<>();

        for (String s:posicoes_ocupadas){
            coordenadas_para_remover = new ArrayList<>();

            ArrayList<String> copia_coordenadas = coordenadas;

            if(mem.containsKey(s)){
                coordenadas_para_remover = mem.get(s);
                lista_arrays_para_remover.add(coordenadas_para_remover);
                continue;
            }

            String[] split = s.split("_");
            String x = split[0];
            String y = split[1];

            for (int i = copia_coordenadas.size()-1; i >= 0; i--) {
                String[] splitc = copia_coordenadas.get(i).split("_");
                String xc = splitc[0];
                String yc = splitc[1];

                if(x.equals(xc))
                    coordenadas_para_remover.add(copia_coordenadas.get(i));
                else
                if(y.equals(yc))
                    coordenadas_para_remover.add(copia_coordenadas.get(i));

            }

            Integer xint = Integer.parseInt(x);
            Integer yint = Integer.parseInt(y);

            int xx = xint;
            int yy = yint;

            while (xx >= 0 && yy >= 0){
                coordenadas_para_remover.add(xx+"_"+yy);
                xx--;
                yy--;
            }

            xx = xint;
            yy = yint;

            while (xx < tamanho && yy < tamanho){
                coordenadas_para_remover.add(xx+"_"+yy);
                xx++;
                yy++;
            }

            xx = xint;
            yy = yint;

            while (xx >= 0 && yy < tamanho){
                coordenadas_para_remover.add(xx+"_"+yy);
                xx--;
                yy++;
            }

            while (xx < tamanho && yy >= 0){
                coordenadas_para_remover.add(xx+"_"+yy);
                xx++;
                yy--;
            }

            lista_arrays_para_remover.add(coordenadas_para_remover);

            mem.put(s, coordenadas_para_remover);
        }

        for (ArrayList<String> arr:lista_arrays_para_remover){
            for (String coord:arr){
                coordenadas.remove(coord);
            }
        }

        return coordenadas.size();
    }

    static ArrayList<String> retorna_coordenadas_da_matriz(int n){
        ArrayList<String> vet = new ArrayList<>();

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                vet.add(j+"_"+i);

        return vet;
    }

    static Long comb(BigInteger n, BigInteger p){
        BigInteger up = fat(n);
        BigInteger bottomleft = fat(p);
        BigInteger bottomright = fat(n.subtract(p));

        return up.divide(bottomleft.multiply(bottomright)).longValue();
    }

    static BigInteger fat(BigInteger n){
        if(n.compareTo(BigInteger.valueOf(1)) <= 0)
            return BigInteger.ONE;
        return n.multiply(fat(n.subtract(BigInteger.ONE)));
    }

    static String[] clear_str_v(String[] v){
        for (int i = 0; i < v.length; i++) {
            v[i] = " ";
        }
        return v;
    }

}
