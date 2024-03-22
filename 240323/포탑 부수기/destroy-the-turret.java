import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static class Node implements Comparable<Node> {
        int r;
        int c;
        int time;
        int rc;
        int strong;
        Node(int r, int c, int strong, int time){
            this.r = r;
            this.c = c;
            this.strong = strong;
            this.time = time;
            this.rc = r+c;
        }

        // 공격자 기준 정렬
        @Override
        public int compareTo(Node o) {
            if(o.strong == this.strong){
                if(this.time == o.time){
                    if(this.rc == o.rc){
                        return o.c - this.c;
                    }else{
                        return o.rc - this.rc;
                    }
                }else{
                    return o.time - this.time;
                }
            }
            return this.strong - o.strong;
        }
    }

    static class findNode{
        int r;
        int c;

        findNode(int r, int c){
            this.r = r;
            this.c = c;
        }
    }
    static int N;
    static int M;
    static int K;
    static int[][] arr;
    static int[][] time;
    static boolean[][] visit;
    static int[][] dir;
    static List<Node> list;
    static boolean[][] isAttacked;

    static int curTime;

    // 우하 좌상
    static int[] dr = {0, 1, 0, -1};
    static int[] dc = {1, 0, -1, 0};

    // 하우 상좌
    static int[] ddr = {0, -1, 0, 1};
    static int[] ddc = {-1, 0, 1, 0};

    static int[] dr8 = {1, 0, -1, 0, 1, 1, -1, -1};
    static int[] dc8 = {0, 1, 0, -1, 1, -1, 1, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        arr = new int[N][M];
        time = new int[N][M];

        for(int r=0; r<N; r++){
            st = new StringTokenizer(br.readLine());
            for(int c=0; c<M; c++){
                arr[r][c] = Integer.parseInt(st.nextToken());
            }
        }

        for(int t=1; t<=K; t++){
            curTime = t;
            find();

            if(list.size() == 1){
                break;
            }
            attack();
            heal();
        }

        int max = 0;

        for(int r=0; r<N; r++){
            for(int c=0; c<M; c++){
                max = Math.max(max, arr[r][c]);
            }
        }
        System.out.println(max);
    }

    public static void find(){
        list = new ArrayList<>();

        for(int r=0; r<N; r++){
            for(int c=0; c<M; c++){
                if(arr[r][c] > 0){
                    list.add(new Node(r, c, arr[r][c], time[r][c]));
                }
            }
        }
        Collections.sort(list);
    }

    public static void attack(){
        Node atk = list.get(0);
        Node def = list.get(list.size()-1);

        time[atk.r][atk.c] = curTime;

        isAttacked = new boolean[N][M];
        arr[atk.r][atk.c] += N+M;

        if(findLoad(atk, def)){
            lazer(atk, def);
        }else{
            bomb(atk, def);
        }
    }

    public static boolean findLoad(Node atk, Node def){
        visit = new boolean[N][M];
        dir = new int[N][M];

        Queue<findNode> q = new LinkedList<>();
        q.offer(new findNode(atk.r, atk.c));
        visit[atk.r][atk.c] = true;

        while (!q.isEmpty()){
            findNode temp = q.poll();

            if(temp.r == def.r && temp.c == def.c){
                return true;
            }

            for(int i=0; i<4; i++){
                int nr = temp.r + dr[i];
                int nc = temp.c + dc[i];

                nr = (nr+N) % N;
                nc = (nc+M) % M;

                if(arr[nr][nc] <= 0){
                    continue;
                }
                if(visit[nr][nc]){
                    continue;
                }
                visit[nr][nc] = true;
                dir[nr][nc] = i;
                q.offer(new findNode(nr, nc));
            }
        }
        return false;
    }

    public static void lazer(Node atk, Node def){
        Queue<findNode> q = new LinkedList<>();
        q.offer(new findNode(def.r, def.c));

        int dmg = atk.strong + N + M;
        int halfDmg = dmg/2;

        while (!q.isEmpty()){
            findNode temp = q.poll();
            isAttacked[temp.r][temp.c] = true;

            if(temp.r == atk.r && temp.c == atk.c){
                break;
            }
            arr[temp.r][temp.c] -= halfDmg;

            int direct = dir[temp.r][temp.c];
            int nr = temp.r + ddr[direct];
            int nc = temp.c + ddc[direct];

            nr = (nr+N) % N;
            nc = (nc+M) % M;

            q.offer(new findNode(nr, nc));
        }

        arr[def.r][def.c] = arr[def.r][def.c] + halfDmg - dmg;

    }

    public static void bomb(Node atk, Node def){
        int dmg = atk.strong + N + M;
        int halfDmg = dmg/2;

        int r = def.r;
        int c = def.c;

        arr[r][c] -= dmg;
        isAttacked[r][c] = true;
        isAttacked[atk.r][atk.c] = true;

        for(int i=0; i<8; i++){
            int nr = r + dr8[i];
            int nc = c + dc8[i];

            nr = (nr + N) % N;
            nc = (nc + M) % M;

            if(nr == atk.r && nc == atk.c){
                continue;
            }
            arr[nr][nc] -= halfDmg;
            isAttacked[nr][nc] = true;
        }
    }

    public static void heal(){
        for(int r=0; r<N; r++){
            for(int c=0; c<M; c++){
                if(isAttacked[r][c]){
                    continue;
                }

                if(arr[r][c]<=0){
                    continue;
                }
                arr[r][c]++;
            }
        }
    }
}