import java.util.Random;

public class new_Tsp {
    double T;
    double t_end;
    double q;
    int L;
    static Result result = new  Result();
    //create_new函数中需要返回多个值，不想用static，于是创建Result类型
    public static class Result {
        int[] city_list;
        int ran1;
        int ran2;
    }

    public new_Tsp(double T, double t_end, double q, int L) {
        this.T = T;
        this.t_end = t_end;
        this.q = q;
        this.L = L;
    }

    public int[] init(int[] city_list) {
        int N = city_list.length;
        for (int i = 0; i < N; i++)
            city_list[i] = i;//初始化解
        return city_list;
    }

    public double distance(double[] city1, double[] city2) {
        double x1 = city1[0];
        double y1 = city1[1];
        double x2 = city2[0];
        double y2 = city2[1];
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public double path_len(int[] city_list, double[][] route) {
        double path = 0;
        int N = city_list.length;
        for (int i = 0; i < N - 1; i++) {
            int index1 = city_list[i];
            int index2 = city_list[i + 1];
            double dis = distance(route[index1], route[index2]);
            path += dis;
        }
        int last_index = city_list[N - 1];
        int first_index = city_list[0];
        double last_dis = distance(route[last_index], route[first_index]);
        path = path + last_dis;
        return path;
    }

    //优化path_len算法，在第一次算出的基础上，经过少量计算求出第二次的path_len
    //如果调换的位置是在首尾两侧，那么就会出现索引越界的问题，需要特别注意
    //如果两个下标挨着，那么计算方案需要改动，改动后的距离算法和之前的不一样
    public double path_len_change(Result result, double[][] route) {
        double reduce_len = 0, add_len = 0,figure = 0;
        int ran1 = result.ran1;
        int ran2 = result.ran2;
        int index1 = result.city_list[ran1];
        int index2 = result.city_list[ran2];
        int index1_last,index1_next,index2_last,index2_next;
        index1_last = index1 - 1;
        index1_next = index1 + 1;
        index2_last = index2 - 1;
        index2_next = index2 + 1;
        if(((index1 ==0) && (index2 == 30)) ||((index1 ==30) && index2 == 0)){
            figure = distance(route[30],route[1]) + distance(route[0],route[29]) -
                    distance(route[0],route[1]) - distance(route[30],route[29]);
            return figure;
        }
        if(((index1 ==0) && (index2 == 1)) ||((index1 ==1) && index2 == 0)){
            figure = distance(route[0],route[2]) + distance(route[1],route[30]) -
                    distance(route[0],route[30]) - distance(route[1],route[2]);
            return figure;
        }
        if(((index1 ==29) && (index2 == 30)) ||((index1 ==30) && index2 == 29)){
            figure = distance(route[28],route[30]) + distance(route[0],route[29]) -
                    distance(route[28],route[29]) - distance(route[0],route[30]);
            return figure;
        }
        if(index1 ==30)
            index1_next = 0;
        if(index1 == 0)
            index1_last = 30;
        if(index2 == 30)
            index2_next = 0;
        if(index2 == 0)
            index2_last = 30;
        if(index1 - index2 == 1){
            figure = distance(route[index1_last],route[index2]) + distance(route[index2_next],route[index1])
                    - distance(route[index1_last],route[index1]) - distance(route[index2_next],route[index2]);
            return figure;
        }
        if(index1 - index2 == -1){
            figure = distance(route[index2_last],route[index2]) + distance(route[index1_next],route[index1])
                    - distance(route[index2_last],route[index1]) - distance(route[index1_next],route[index2]);
            return figure;
        }
        add_len = distance(route[index1_last],route[index2]) + distance(route[index1_next],route[index2])
                + distance(route[index2_last],route[index1]) + distance(route[index2_next],route[index1]);
        reduce_len = distance(route[index1_last],route[index1]) + distance(route[index1_next],route[index1])
                + distance(route[index2_last],route[index2]) + distance(route[index2_next],route[index2]);
        figure = add_len - reduce_len;
        return figure;
    }

    Result create_new(int[] city_list) {
        Random r = new Random();
        int ran1 = r.nextInt(31);
        int ran2 = r.nextInt(31);
        int tmp = city_list[ran1];
        city_list[ran1] = city_list[ran2];
        city_list[ran2] = tmp;
        result.city_list = city_list;
        result.ran1 = ran1;
        result.ran2 = ran2;
        return result;
    }

    int[] figure_out(int[] city_list, double[][] route) {
        int[] city_list_copy;
        double f1,f2,df,df2;
        int count =0;
        Random rn = new Random();
        double r; // 0-1之间的随机数，用来决定是否接受新解
        while (T >= t_end) {
            for (int i = 0; i < L; i++) {
                city_list_copy = city_list.clone();
                result = create_new(city_list);
                city_list = result.city_list;
                f1 = path_len(city_list_copy,route);
                f2 = path_len(city_list,route);
                df = f2- f1;
                df2 = path_len_change(result,route);
                if (df >= 0) {
                    r = rn.nextDouble();
                    if (Math.exp(-df/T) <= r)
                        city_list = city_list_copy;
                }
            }
            T *= q;
        }
        return city_list;
    }
}
