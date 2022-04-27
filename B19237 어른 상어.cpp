#include <iostream>
#include <algorithm>
using namespace std;
struct info
{
	int num, dir, x, y, k;
	info() {}
	info(int num, int k) { this->num = num; this->k = k; }
	info(int x, int y, int dir) { this->x = x; this->y = y; this->dir = dir; }
};
int N, M, k, res = -1;
int dx[5] = { 0,-1,1,0,0 }, dy[5] = { 0,0,0,-1,1 }; // 상하좌우
info arr[21][21];
info shark[401];
int priority[401][5][5];
int main() {
	cin >> N >> M >> k;
	for (int i = 0; i < N; ++i) {
		for (int j = 0; j < N; ++j) {
			int n;
			cin >> n;
			if (n > 0) {
				shark[n].x = i, shark[n].y = j;
				arr[i][j] = { n,k };
			}
		}
	}
	for (int i = 1; i <= M; ++i) cin >> shark[i].dir;
	for (int i = 1; i <= M; ++i) for (int j = 1; j <= 4; ++j) for (int m = 0; m < 4; ++m) cin >> priority[i][j][m];
	for (int t = 1; t <= 1000; ++t) {
		int moved[21][21] = { 0 };
		for (int n = 1; n <= M; ++n) {
			if (shark[n].dir == 0) continue; // 격자 밖으로 나간 상어
			int xx = shark[n].x, yy = shark[n].y;
			bool chk = false;
			for (int i = 0; i < 4; ++i) {
				int x = xx + dx[priority[n][shark[n].dir][i]], y = yy + dy[priority[n][shark[n].dir][i]];
				if (x < 0 || x >= N || y < 0 || y >= N || arr[x][y].k > 0) continue;
				if (moved[x][y] == 0) moved[x][y] = n;
				else moved[x][y] = min(moved[x][y], n);
				shark[n].dir = priority[n][shark[n].dir][i];
				chk = true;
				break;
			}
			if (chk) continue;
			for (int i = 0; i < 4; ++i) {
				int x = xx + dx[priority[n][shark[n].dir][i]], y = yy + dy[priority[n][shark[n].dir][i]];
				if (x < 0 || x >= N || y < 0 || y >= N || arr[x][y].num != n) continue;
				if (moved[x][y] == 0) moved[x][y] = n;
				else moved[x][y] = min(moved[x][y], n);
				shark[n].dir = priority[n][shark[n].dir][i];
				chk = true;
				break;
			}
			if (chk) continue;
			moved[xx][yy] = min(moved[xx][yy], n);
		}
		bool num[401] = { 0 };
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j) {
				--arr[i][j].k;
				if (arr[i][j].k == 0) arr[i][j].num = 0;
				if (moved[i][j] == 0) continue;
				num[moved[i][j]] = true;
				arr[i][j] = { moved[i][j],k };
				shark[moved[i][j]].x = i, shark[moved[i][j]].y = j;
			}
		}
		int cnt = 0;
		for (int i = 1; i <= M; ++i) {
			if (num[i]) {
				++cnt;
				continue;
			}
			shark[i] = { 0,0,0 };
		}
		if (num[1] && cnt == 1) {
			res = t;
			break;
		}
	}
	cout << res;
	return 0;
}