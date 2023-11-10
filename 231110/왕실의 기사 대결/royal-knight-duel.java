import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {

    static int L, N, Q;
    static int[][] map;

    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, 1, 0, -1};


    public static class Knight{
        int r;
        int c;
        int h;
        int w;

        Knight(int r, int c, int h, int w){
            this.r = r;
            this.c = c;
            this.h = h;
            this.w = w;
        }
    }

    static Knight[] knights;
    static int[] endurance;
    static int[] initEndurance;
    static boolean[] moveKnights;
    static int[] demage;


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        L = sc.nextInt();
        N = sc.nextInt();
        Q = sc.nextInt();

        map = new int[L][L];

        for(int r=0; r<L; r++){
            for(int c=0; c<L; c++){
                map[r][c] = sc.nextInt();
            }
        }

        knights = new Knight[N+1];
        endurance = new int[N+1];
        initEndurance = new int[N+1];

        for(int i=1; i<=N; i++){
            int r = sc.nextInt()-1;
            int c = sc.nextInt()-1;
            int h = sc.nextInt();
            int w = sc.nextInt();
            int k = sc.nextInt();

            knights[i] = new Knight(r,c,h,w);
            endurance[i] = initEndurance[i] = k;
        }

        for(int t=0; t<Q; t++){
            int i = sc.nextInt();
            int d = sc.nextInt();

            if(isPosMove(i, d)){
                // 이동
                for(int j=1; j<=N; j++){
                    if(!moveKnights[j]){
                        continue;
                    }
                    endurance[j] -= demage[j];
                    knights[j].r += dr[d];
                    knights[j].c += dc[d];
                }
            }
        }

        int sum = 0;
        for(int i=1; i<=N; i++){
            if(endurance[i]>0){
                sum += initEndurance[i]-endurance[i];
            }
        }

        System.out.println(sum);

    }


    public static boolean isPosMove(int idx, int dir){

        // 초기화 작업
        moveKnights = new boolean[N+1];
        demage = new int[N+1];

        Queue<Integer> q = new LinkedList<>();
        q.offer(idx);
        moveKnights[idx] = true;

        while(!q.isEmpty()){
            int t = q.poll();
            Knight temp = knights[t];

            // 기사 이동 경로가 범위가 넘어가는가?
            int nr = temp.r + dr[dir];
            int nc = temp.c + dc[dir];

            int fr = nr+temp.w -1;
            int fc = nc+temp.h -1;

            if(nr<0 || nc < 0 || fr >= L || fc >= L){
                return false;
            }
            for(int r=nr; r<=fr; r++){
                for(int c=nc; c<=fc; c++){
                    if(map[r][c] == 1){
                        demage[t]++;
                    }
                    if(map[r][c] == 2){
                        return false;
                    }
                }
            }

            for(int i= 1; i<=N; i++){
                Knight next = knights[i];
                if(i == t){
                    continue;
                }
                // 만약 범위안에 다른 기사가 있을 경우
                int nextFr = next.r + next.h -1;
                int nextFc = next.c + next.w -1;

                if(endurance[i] <= 0){
                    continue;
                }
                if(nextFr < nr || next.r > fr){
                    continue;
                }
                if(nextFc < nc || next.c > fc){
                    continue;
                }
                q.offer(i);
                moveKnights[i] = true;
            }
        }
        
        return true;
    }
}