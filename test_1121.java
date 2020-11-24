import java.util.Random;

public class test_1121 {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        new_Tsp my_Tsp = new new_Tsp(50000.0,1e-8,0.98,1000);
        double[][] route = {
                {1304,2312},{3639,1315},{4177,2244},{3712,1399},
                {3488,1535},{3326,1556},{3238,1229},{4196,1004},
                {4312,790},{4386,570},{3007,1970},{2562,1756},
                {2788,1491},{2381,1676},{1332,695}, {3715,1678},
                {3918,2179},{4061,2370}, {3780,2212},{3676,2578},
                {4029,2838}, {4263,2931},{3429,1908},{3507,2367},
                {3394,2643},{3439,3201},{2935,3240}, {3140,3550},
                {2545,2357},{2778,2826}, {2370,2975}};
        int N = route.length;
        int count =0;
        int[] city_list = new int[N];
        city_list = my_Tsp.init(city_list);
        city_list = my_Tsp.figure_out(city_list,route);
        long end = System.currentTimeMillis();
        System.out.printf("模拟退火算法，初始温度T0=%.2f,降温系数q=%.2f,每个温度迭代%d次,共降温%d次，",my_Tsp.T,my_Tsp.q,my_Tsp.L,count);
        for(int i=0;i<N-1;i++){
            System.out.printf("%d--->",city_list[i]);
        }
        System.out.printf("%d\n",city_list[N-1]);
        double len = my_Tsp.path_len(city_list,route); // 最优路径长度
        System.out.printf("最优路径长度为:%f\n",len);
        System.out.println("程序运行耗时秒.\n"+(end - start)/1000.0);
    }
}

