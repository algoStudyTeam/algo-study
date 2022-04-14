#include <iostream>
#include <queue>
#include <vector>
#include <cstring>
using namespace std;
struct info {
	int x, y;
	info() {}
	info(int x, int y) { this->x = x; this->y = y; }
	bool operator()(info a, info b) {
		if (a.x == b.x) return a.y > b.y;
		return a.x > b.x;
	}
};
int N, X, Y, res, fsize = 2, ate;
int arr[21][21];
int dx[4] = { -1,1,0,0 }, dy[4] = { 0,0,-1,1 };
bool visited[21][21];
queue<info> q;
priority_queue<info, vector<info>, info> pq;
int main() {
	cin >> N;
	for (int i = 0; i < N; ++i) {
		for (int j = 0; j < N; ++j) {
			cin >> arr[i][j];
			if (arr[i][j] == 9) X = i, Y = j, arr[i][j] = 0;
		}
	}
	while (1) {
		memset(visited, false, sizeof(visited));
		q.push({ X,Y }); // 현재 아기 상어 위치부터 시작
		visited[X][Y] = true;
		int cnt = 1;
		while (1) {
			while (!q.empty()) { // 이동할 수 있는 위치 탐색해서 가능한 위치를 pq에 담음
				for (int i = 0; i < 4; ++i) {
					int x = q.front().x + dx[i], y = q.front().y + dy[i];
					if (x < 0 || x >= N || y < 0 || y >= N || arr[x][y] > fsize || visited[x][y]) continue;
					pq.push({ x,y });
					visited[x][y] = true;
				}
				q.pop();
			}
			if (pq.empty()) { // 이동할 수 있는 위치 없으면 끝
				cout << res;
				return 0;
			}
			X = Y = -1;
			while (!pq.empty()) {
				if (X == -1 && arr[pq.top().x][pq.top().y] > 0 && arr[pq.top().x][pq.top().y] < fsize) {
					X = pq.top().x, Y = pq.top().y;
					arr[X][Y] = 0;
					if (++ate == fsize)ate = 0, ++fsize;
					break;
				}
				else {
					q.push(pq.top());
					visited[pq.top().x][pq.top().y] = true;
				}
				pq.pop();
			}
			if (X != -1) break; // 이동할 수 있는 곳에 먹을 수 있는 물고기가 있다면 빠져나가기
			++cnt; // 없다면 한 칸 더 이동
		}
		res += cnt; // 하나 먹었으니까 움직인 만큼 res에 추가
		pq = {}, q = {};
	}
	return 0;
}
