package algo220425_0501;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

class DragonCurve {
	int x;
	int y;
	int d;
	int g;
	
	public DragonCurve(int x, int y, int d, int g) {
		super();
		this.x = x;
		this.y = y;
		this.d = d;
		this.g = g;
	}
	
}
public class BOJ_Gold4_15685_드래곤커브 {
	
	static int N;
	static boolean map[][] = new boolean[105][105];
	static List<Integer> dirList = new ArrayList<Integer>();
	static DragonCurve dragonCurves[] = new DragonCurve[25];
	static int dx[] = {1,0,-1,0};
	static int dy[] = {0,-1,0,1};

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		N = Integer.parseInt(in.readLine());

		StringTokenizer st = null;
		
		for (int idx = 1; idx <= N; idx++) {
			st = new StringTokenizer(in.readLine());
			
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			int g = Integer.parseInt(st.nextToken());
			
			dragonCurves[idx] = new DragonCurve(x, y, d, g);
		}
		
		// process
		makeDragonCurves();
		
		System.out.println(findSquareCnt());
	}

	private static int findSquareCnt() {
		int cnt = 0;
		for (int x = 0; x <= 100; x++) {
			for (int y = 0; y <= 100; y++) {
				if (x + 1 > 100 || y + 1 > 100)
					break;

				if (map[x][y] && map[x + 1][y + 1] && map[x + 1][y] && map[x][y + 1]) {
					++cnt;
				}
			}
		}
		
		return cnt;
	}

	private static void makeDragonCurves() {
		for (int idx = 1; idx <= N; idx++) {
			DragonCurve curDragonCurve = dragonCurves[idx];
			int Gen = curDragonCurve.g;
			dirList.clear();
			dirList.add(curDragonCurve.d);
			
			int curX = curDragonCurve.x;
			int curY = curDragonCurve.y;
			map[curY][curX] = true;
			
			curX += dx[curDragonCurve.d];
			curY += dy[curDragonCurve.d];
			map[curY][curX] = true;
			
			for (int curGen = 1; curGen <= Gen; curGen++) {

				int dirListSize = dirList.size();
				for (int dIdx = dirListSize - 1; dIdx >= 0; dIdx--) {
					int curDir = (dirList.get(dIdx) + 1) % 4;
					curX += dx[curDir];
					curY += dy[curDir];
					
					map[curY][curX] = true;
					dirList.add(curDir);
				}
			}
		}
	}

}
