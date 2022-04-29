#include <iostream>
#include <queue>
#include <algorithm>
using namespace std;
int N, M, K, res;
int arr[21][21], score[21][21], info[3], dice[6] = { 2,1,5,6,4,3 };
int dx[4] = { 0,1,0,-1 }, dy[4] = { 1,0,-1,0 }, dd[4] = { 2,3,0,1 }; // 우하좌상
queue<pair<int, int>> q;
int main() {
	cin >> N >> M >> K;
	for (int i = 0; i < N; ++i) for (int j = 0; j < M; ++j) cin >> arr[i][j];
	// 각 칸마다 얻을 수 있는 점수 미리 구해놓기
	for (int i = 0; i < N; ++i) {
		for (int j = 0; j < M; ++j) {
			bool visited[21][21] = { false };
			visited[i][j] = true;
			int cnt = 1;
			q.push({ i,j });
			while (!q.empty()) {
				for (int k = 0; k < 4; ++k) {
					int x = q.front().first + dx[k], y = q.front().second + dy[k];
					if (x < 0 || x >= N || y < 0 || y >= M || visited[x][y]) continue;
					visited[x][y] = true;
					if (arr[x][y] == arr[i][j]) q.push({ x,y }), ++cnt;
				}
				q.pop();
			}
			score[i][j] = arr[i][j] * cnt;
		}
	}
	for (int k = 0; k < K; ++k) {
		int x = info[0] + dx[info[2]], y = info[1] + dy[info[2]];
		if (x < 0 || x >= N || y < 0 || y >= M) info[2] = dd[info[2]], x = info[0] + dx[info[2]], y = info[1] + dy[info[2]]; // 범위 밖이면 반대 방향으로 한 칸
		res += score[x][y];
		if (info[2] == 0) swap(dice[1], dice[5]), swap(dice[1], dice[3]), swap(dice[1], dice[4]); // 우
		else if (info[2] == 1) swap(dice[3], dice[0]), swap(dice[3], dice[1]), swap(dice[3], dice[2]); // 하
		else if (info[2] == 2) swap(dice[5], dice[1]), swap(dice[5], dice[4]), swap(dice[5], dice[3]); // 좌
		else swap(dice[0], dice[3]), swap(dice[0], dice[2]), swap(dice[0], dice[1]); // 상
		if (dice[3] > arr[x][y]) info[2] = (info[2] + 1) % 4;
		else if (dice[3] < arr[x][y]) info[2] = (info[2] + 3) % 4;
		info[0] = x, info[1] = y;
	}
	cout << res;
	return 0;
}