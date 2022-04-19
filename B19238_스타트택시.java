package algo220418_0424;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * 4.18 11:11 - 
 * 
 * 스타트 택시
 * 
 * 손님을 도착지로 데려다줄 때마다 연료 충전, 연료가 바닥나면 그 날의 업무가 끝난다.
 * 
 * M명의 승객 - NxN 격자로 나타낼 수 있다.
 * 최단경로로만 이동
 * 
 * 여러 승객이 같이 탑승하는 경우는 없다. -> M번 반복해야 한다.
 * 
 * 현재 위치에서 최단거리가 가장 짧은 승객 고름 -> 행 번호가 가장 작은 승객 -> 열 번호가 가장 작은 승객
 * 
 * 택시, 승객이 같은 위치에 서 있으면 그 승객까지의 최단거리 = 0
 * 연료 -> 한 칸 이동할 때마다 1만큼 소모
 * 
 * 목적지로 이동시키면, 소모한 연료 양의 두 배가 충전
 * 이동하는 도중에 연료 바닥나면 이동 실패, 업무 끝
 * 
 *
 * ####
 * 목적지로 이동시키지 못하는 경우도 고려해야함! 
 */
class Person implements Comparable<Person> {
	int idx;
	int startR;
	int startC;
	int destR;
	int destC;
	
	public Person(int idx, int startR, int startC, int destR, int destC) {
		super();
		this.idx = idx;
		this.startR = startR;
		this.startC = startC;
		this.destR = destR;
		this.destC = destC;
	}

	@Override
	public int compareTo(Person o) {
		if (this.startR - o.startR == 0) {
			return this.startC - o.startC;
		} else {
			return this.startR - o.startR;
		}
	}
	
}

public class BOJ_Gold3_19238_스타트택시 {

	static int map[][], dist[][], start[][], N, M, gasoline, taxiR, taxiC, moveToPersonDist;
	static List<Person> persons = new ArrayList<Person>();
	static Queue<int[]> queue = new LinkedList<int[]>();
	static Person closePerson;
	static PriorityQueue<Person> closePersons = new PriorityQueue<Person>();
	
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		gasoline = Integer.parseInt(st.nextToken());
		
		map = new int[N][N];
		start = new int[N][N];
		dist = new int[N][N];
		
		for (int r = 0; r < N; r++) {
			st = new StringTokenizer(in.readLine(), " ");
			for (int c = 0; c < N; c++) {
				map[r][c] = Integer.parseInt(st.nextToken());
			}
		}
		
		st = new StringTokenizer(in.readLine(), " ");
		taxiR = Integer.parseInt(st.nextToken())-1;
		taxiC = Integer.parseInt(st.nextToken())-1;
		map[taxiR][taxiC] = -1; // 택시 -1로 표시
		
		// 승객 2~M+1으로 표시
		for (int idx = 1; idx <= M; idx++) {
			st = new StringTokenizer(in.readLine(), " ");
			int startR = Integer.parseInt(st.nextToken())-1;
			int startC = Integer.parseInt(st.nextToken())-1;
			int destR = Integer.parseInt(st.nextToken())-1;
			int destC = Integer.parseInt(st.nextToken())-1;
			persons.add(new Person(idx, startR, startC, destR, destC));
			start[startR][startC] = idx;
		}
		
		for (int i = 0; i < M; i++) {
			if (gasoline == 0) {
				System.out.println("-1");
				return;
			}
			// 최단경로 찾기
			findDist();
			
			if (closePersons.size() == 0) {
				System.out.println("-1");
				return;
			}
			
			closePerson = closePersons.poll();
			
			// 가장 가까운 승객으로 이동
			if (!moveClosePerson()) {
				System.out.println("-1");
				return;
			}
			
			// 목적지로 이동
			if (!moveDestination()) {
				System.out.println("-1");
				return;
			}
		}
		
		System.out.println(gasoline);

	}

	private static boolean moveDestination() {
		queue.clear();

		for (int r = 0; r < N; r++) {
			Arrays.fill(dist[r], -1);
		}

		queue.offer(new int[] { taxiR, taxiC });
		dist[taxiR][taxiC] = 0;

		int moveDist = 0;
		boolean isFind = false;
		
		while (!queue.isEmpty()) {
			int[] cur = queue.poll();

			for (int dir = 0; dir < 4; dir++) {
				int nr = cur[0] + dr[dir];
				int nc = cur[1] + dc[dir];

				if (!checkBoundary(nr, nc) || map[nr][nc] == 1 || dist[nr][nc] != -1)
					continue;

				queue.offer(new int[] { nr, nc });
				dist[nr][nc] = dist[cur[0]][cur[1]] + 1;

				if (closePerson.destR == nr && closePerson.destC == nc) {
					moveDist = dist[nr][nc];
					isFind = true;
					break;
				}
			}
			
			if (isFind) break;
		}
		
		if (!isFind) return false; // 목적지에 손님을 이동시킬 수 없는 경우! 이거 안써서 틀림
		
		if (gasoline - moveDist < 0) {
			return false;
		}
		
		gasoline -= moveDist;
		gasoline += (moveDist * 2);
		
		map[taxiR][taxiC] = 0;
		map[closePerson.destR][closePerson.destC] = -1;
		taxiR = closePerson.destR;
		taxiC = closePerson.destC;

		persons.remove(closePerson);
		
		return true;
	}

	private static boolean moveClosePerson() {
		
		if (gasoline - moveToPersonDist < 0) {
			return false;
		}
		
		gasoline -= moveToPersonDist;
		
		map[taxiR][taxiC] = 0;
		map[closePerson.startR][closePerson.startC] = -1;
		taxiR = closePerson.startR;
		taxiC = closePerson.startC;
		start[closePerson.startR][closePerson.startC] = 0;
		
		return true;
	}

	static int dr[] = {-1,1,0,0};
	static int dc[] = {0,0,1,-1};
	private static void findDist() {
		queue.clear();
		closePersons.clear();
		if (start[taxiR][taxiC] != 0) {
			for (int i = 0; i < persons.size(); i++) {
				if (persons.get(i).idx == start[taxiR][taxiC]) {
					closePersons.add(persons.get(i));
					moveToPersonDist = 0;
					return;
				}
			}
		}
		
		for (int r = 0; r < N; r++) {
			Arrays.fill(dist[r], -1);
		}
		
		queue.offer(new int[] {taxiR, taxiC});
		dist[taxiR][taxiC] = 0;
		
		while (!queue.isEmpty()) {
			int size = queue.size();
			
			for (int a = 0; a < size; a++) {
				int[] cur = queue.poll();
				
				for (int dir = 0; dir < 4; dir++) {
					int nr = cur[0] + dr[dir];
					int nc = cur[1] + dc[dir];
					
					if (!checkBoundary(nr, nc) || map[nr][nc] == 1 || dist[nr][nc] != -1) continue;
					
					queue.offer(new int[] {nr, nc});
					dist[nr][nc] = dist[cur[0]][cur[1]] + 1;
					
					if (start[nr][nc] != 0) {
						for (int i = 0; i < persons.size(); i++) {
							if (persons.get(i).idx == start[nr][nc]) {
								closePersons.add(persons.get(i));
								moveToPersonDist = dist[nr][nc];
								break;
							}
						}
					}
				}
			}
			
			if (closePersons.size() != 0) return;
		}
		
		return;
	}
	
	private static boolean checkBoundary(int r, int c) {
		return r>=0 && r<N && c>=0 && c<N;
	}

}
