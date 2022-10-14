import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class bt {

    static ArrayList<String> testes = new ArrayList<>();

    //static HashMap<String,ArrayList<String>> memoria_visao_coordenada = new HashMap<>();

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

    public static void load_tests(String path) throws IOException {
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
            load_tests("./tests.txt");
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Running Tests");

        for (int i = 0; i < testes.size(); i++){

            String[] split = testes.get(i).split(",");

            int n = Integer.parseInt(split[0]);
            int p = Integer.parseInt(split[1]);
            int g = Integer.parseInt(split[2]);
            long result = Long.parseLong(split[3]);

            long tempoInicial = System.currentTimeMillis();

            boolean passed = unit_test(n, p, g,result);

            long tempoFinal = System.currentTimeMillis();

            System.out.println(passed+"  | "+String.format("%.3f ms", (tempoFinal - tempoInicial) / 1000d)+" |  Input[ "+n+", "+p+", "+g+", "+result+" ]");

        }
    }

    static boolean unit_test(int n, int p, int g, long result){
        return exec(n, p, g) == result;
    }

    static long exec(int n, int p, int g){
        return c(clear_str_v(new String[n*n]),0, p, g);
    }

    static long combinatoria_galinhas(String[] v, int tamanho, int qtd_galinhas){
        ArrayList<String> coordenadas = retorna_coordenadas_da_matriz(tamanho);

        coordenadas = filtra_coordenada(v,coordenadas,tamanho);

        if(qtd_galinhas < coordenadas.size())
            return c_pura(clear_str_v(new String[coordenadas.size()]),0,qtd_galinhas);
        else
        if(qtd_galinhas == coordenadas.size())
            return 1;
        else
            return 0;
    }

    static long c(String[] v, int atual, int porquinhos, int galinhas){
        int r = 0;

        if(porquinhos == 0)
            return combinatoria_galinhas(v,(int)Math.sqrt(v.length),galinhas);

        for (int i = atual; i < v.length; i++) {
            if(v[i].equals("p"))
                continue;
            v[i] = "p";
            r += c(v,i+1,porquinhos-1,galinhas);
            v[i] = " ";
        }

        return r;
    }

    static long c_pura(String[] v, int atual, int elementos){
        int r = 0;

        if(elementos == 0)
            return 1;

        for (int i = atual; i < v.length; i++) {
            if(v[i].equals("p"))
                continue;
            v[i] = "p";
            r += c_pura(v,i+1,elementos-1);
            v[i] = " ";
        }

        return r;
    }


    static ArrayList<String> filtra_coordenada(String[] v, ArrayList<String> coordenadas, int tamanho){
        ArrayList<String> r = new ArrayList<>();

        ArrayList<String> posicoes_ocupadas = new ArrayList<>();

        for (int i = 0; i < v.length; i++)
            if(v[i] != " ")
                posicoes_ocupadas.add(coordenadas.get(i));

        for (String s:posicoes_ocupadas){

            String[] split = s.split("-");
            String x = split[0];
            String y = split[1];


            for (int i = coordenadas.size()-1; i >= 0; i--) {
                String[] splitc = coordenadas.get(i).split("-");
                String xc = splitc[0];
                String yc = splitc[1];

                if(x.equals(xc))
                    coordenadas.remove(i);
                else
                if(y.equals(yc))
                    coordenadas.remove(i);

            }

            Integer xi = Integer.parseInt(x);
            Integer yi = Integer.parseInt(y);

            while (xi <= tamanho - 1 || yi <= tamanho - 1){
                String coord = xi+"-"+yi;
                coordenadas.remove(coord);
                xi++;
                yi++;
            }

            while (xi >= 0 || yi >= 0){
                String coord = xi+"-"+yi;
                coordenadas.remove(coord);
                xi--;
                yi--;
            }

            xi = Integer.parseInt(x);
            yi = Integer.parseInt(y);

            while (xi >= 0 || yi <= tamanho - 1){
                String coord = xi+"-"+yi;
                coordenadas.remove(coord);
                xi--;
                yi++;
            }

            xi = Integer.parseInt(x);
            yi = Integer.parseInt(y);

            while (xi <= tamanho - 1 || yi >= 0){
                String coord = xi+"-"+yi;
                coordenadas.remove(coord);
                xi++;
                yi--;
            }

        }

        return coordenadas;
    }

    static ArrayList<String> retorna_coordenadas_da_matriz(int n){
        ArrayList<String> vet = new ArrayList<>();

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                vet.add(i+"-"+j);

        return vet;
    }

    static String[] clear_str_v(String[] v){
        for (int i = 0; i < v.length; i++) {
            v[i] = " ";
        }
        return v;
    }

    static void dump_str_v(String[] v){
        for (String s:v)
            System.out.print("["+s+"]");
        System.out.println();
    }

    static void dump_str_m(String[][] m){
        System.out.println();
        for (String[] c:m){
            for (String i:c){
                System.out.print("["+i+"]");
            }
            System.out.println();
        }
        System.out.println();
    }
}
