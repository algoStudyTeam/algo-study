#include <iostream>
#include <string>
#include <queue>
#include <vector>
#include <algorithm>
#include <cstdlib>
using namespace std;
struct info {
	int x, y, dept;
	info() {}
	info(int x, int y, int dept) { this->x = x; this->y = y; this->dept = dept; }
};
int R, C, K, W, res = 101;
int dx[5] = { 0,0,0,-1,1 }, dy[5] = { 0,1,-1,0,0 }; // 우좌상하
int dd[5][3] = { {0,0,0},{3,0,4},{3,0,4},{2,0,1},{2,0,1} };
int arr[22][22], temp[22][22], add[22][22], tmp[22][22];
bool wall[22][22][22][22];
vector<pair<int, int> > research;
queue<pair<int, int> > heater;
int main() {
	cin >> R >> C >> K;
	for (int i = 1; i <= R; ++i) {
		for (int j = 1; j <= C; ++j) {
			cin >> arr[i][j];
			if (arr[i][j] == 5) research.push_back({ i,j });
			else if (arr[i][j] > 0) heater.push({ i,j });
		}
	}
	cin >> W;
	for (int i = 0; i < W; ++i) {
		int x, y, t;
		cin >> x >> y >> t;
		if (t) wall[x][y][x][y + 1] = wall[x][y + 1][x][y] = true;
		else wall[x][y][x - 1][y] = wall[x - 1][y][x][y] = true;
	}
	// 온풍기 틀 때 올라가는 온도를 구해서 add에 미리 저장해놓음
	while (!heater.empty()) {
		int x = heater.front().first, y = heater.front().second, d = arr[x][y];
		heater.pop();
		x += dx[d], y += dy[d];
		if (x<1 || x>R || y<1 || y>C) continue; // 범위 밖이면 넘김
		if (wall[x - dx[d]][y - dy[d]][x][y]) continue;
		queue<info> q;
		q.push({ x,y,5 });
		bool visited[22][22] = { 0 };
		visited[x][y] = true;
		while (!q.empty()) {
			add[q.front().x][q.front().y] += q.front().dept;
			if (q.front().dept == 1) {
				q.pop();
				continue;
			}
			for (int k = -1; k <= 1; ++k) {
				int xx = q.front().x + dx[d], yy = q.front().y + dy[d];
				if (d <= 2) xx += k;
				else yy += k;
				if (xx<1 || xx>R || yy<1 || yy>C || visited[xx][yy]) continue; // 범위 밖이거나 이미 방문한 곳이면 넘김
				if (k == 0) {
					if (wall[xx][yy][q.front().x][q.front().y]) continue;
				}
				else {
					int xxx = q.front().x + dx[dd[d][k + 1]], yyy = q.front().y + dy[dd[d][k + 1]];
					if (wall[q.front().x][q.front().y][xxx][yyy] || wall[xx][yy][xxx][yyy]) continue;
				}
				visited[xx][yy] = true;
				q.push({ xx,yy,q.front().dept - 1 });
			}
			q.pop();
		}
	}

	for (int t = 1; t <= 100; ++t) {
		// 1. 온풍기 틀기
		for (int i = 1; i <= R; ++i) for (int j = 1; j <= C; ++j) temp[i][j] += add[i][j];

		// 2. 온도 조절
		copy(&temp[0][0], &temp[0][0] + 22 * 22, &tmp[0][0]);
		bool chk[22][22][22][22] = { 0 };
		for (int i = 1; i <= R; ++i) {
			for (int j = 1; j <= C; ++j) {
				for (int k = 1; k <= 4; ++k) {
					int x = i + dx[k], y = j + dy[k];
					if (x<1 || x>R || y<1 || y>C || chk[i][j][x][y] || wall[i][j][x][y]) continue;
					chk[i][j][x][y] = chk[x][y][i][j] = true;
					int val = abs(temp[i][j] - temp[x][y]) / 4;
					if (temp[i][j] < temp[x][y]) tmp[i][j] += val, tmp[x][y] -= val;
					else tmp[i][j] -= val, tmp[x][y] += val;
				}
			}
		}
		copy(&tmp[0][0], &tmp[0][0] + 22 * 22, &temp[0][0]);

		// 3. 바깥 온도 감소
		for (int i = 1; i <= R; ++i) {
			if (temp[i][1] >= 1) temp[i][1] -= 1;
			if (temp[i][C] >= 1) temp[i][C] -= 1;
		}
		for (int i = 2; i < C; ++i) {
			if (temp[1][i] >= 1) temp[1][i] -= 1;
			if (temp[R][i] >= 1) temp[R][i] -= 1;
		}

		// 4. 초콜릿 먹음
		// 5. 조사하는 칸 온도 모두 K 이상인지 검사
		int cnt = 0;
		for (int i = 0; i < research.size(); ++i) {
			if (temp[research[i].first][research[i].second] >= K) ++cnt;
			else break;
		}
		if (cnt == research.size()) {
			res = t;
			break;
		}
	}
	cout << res;
	return 0;
}