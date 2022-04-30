#include <iostream>
#include <vector>
#include <queue>
#include <cstring>
using namespace std;
int N, M, res[4];
int arr[51][51], ds[101][2];
int dx[5] = { 0,-1,1,0,0 }, dy[5] = { 0,0,0,-1,1 }, dd[5] = { 0,3,4,2,1 };
vector<int> order;
queue<int> q, newball;
queue<pair<int, int> > bomb, tmp;
void fillEmpty() {
	int x = (N + 1) / 2, y = x, d = 3;
	for (int i = 0; i < order.size(); ++i) {
		for (int j = 0; j < order[i]; ++j) {
			x += dx[d], y += dy[d];
			if (arr[x][y] == 0) continue;
			q.push(arr[x][y]);
		}
		d = dd[d];
	}
	memset(arr, 0, sizeof(arr));
	x = (N + 1) / 2, y = x, d = 3;
	for (int i = 0; i < order.size(); ++i) {
		for (int j = 0; j < order[i]; ++j) {
			if (q.empty()) break;
			x += dx[d], y += dy[d];
			arr[x][y] = q.front();
			q.pop();
		}
		if (q.empty()) break;
		d = dd[d];
	}
	q = {};
}
bool Bomb() {
	int x = (N + 1) / 2, y = x, d = 3, val = -1, cnt = 0, chk = 0;
	for (int i = 0; i < order.size(); ++i) {
		for (int j = 0; j < order[i]; ++j) {
			x += dx[d], y += dy[d];
			if (arr[x][y] == 0) {
				chk = 1;
				break;
			}
			if (val == -1) val = arr[x][y], cnt = 1, tmp.push({ x,y });
			else if (val == arr[x][y]) ++cnt, tmp.push({ x,y });
			else {
				if (cnt >= 4) while (!tmp.empty()) bomb.push(tmp.front()), tmp.pop();
				tmp = {};
				tmp.push({ x,y });
				val = arr[x][y], cnt = 1;
			}
		}
		if (chk) break;
		d = dd[d];
	}
	if (cnt >= 4) while (!tmp.empty()) bomb.push(tmp.front()), tmp.pop();
	tmp = {};
	if (bomb.empty()) return true;
	while (!bomb.empty()) ++res[arr[bomb.front().first][bomb.front().second]], arr[bomb.front().first][bomb.front().second] = 0, bomb.pop();
	return false;
}
void changeBall() {
	int x = (N + 1) / 2, y = x, d = 3, val = -1, cnt = 0, chk = 0;
	for (int i = 0; i < order.size(); ++i) {
		for (int j = 0; j < order[i]; ++j) {
			x += dx[d], y += dy[d];
			if (arr[x][y] == 0) {
				chk = 1;
				break;
			}
			if (val == -1) val = arr[x][y], cnt = 1;
			else if (val == arr[x][y]) ++cnt;
			else {
				newball.push(cnt);
				newball.push(val);
				val = arr[x][y], cnt = 1;
			}
		}
		if (chk) break;
		d = dd[d];
	}
	if (cnt > 0) {
		newball.push(cnt);
		newball.push(val);
	}
	memset(arr, 0, sizeof(arr));
	x = (N + 1) / 2, y = x, d = 3;
	for (int i = 0; i < order.size(); ++i) {
		for (int j = 0; j < order[i]; ++j) {
			if (newball.empty()) break;
			x += dx[d], y += dy[d];
			arr[x][y] = newball.front();
			newball.pop();
		}
		if (newball.empty()) break;
		d = dd[d];
	}
	newball = {};
}
int main() {
	cin >> N >> M;
	for (int i = 1; i <= N; ++i)for (int j = 1; j <= N; ++j)cin >> arr[i][j];
	for (int i = 0; i < M; ++i) cin >> ds[i][0] >> ds[i][1];
	for (int i = 1; i < N; ++i) order.push_back(i), order.push_back(i);
	order.push_back(N - 1);
	for (int m = 0; m < M; ++m) {
		// 1. »ó¾î¿¡¼­ d¹æÇâ s°Å¸® ±¸½½µé ÆÄ±«
		int d = ds[m][0], s = ds[m][1], x = (N + 1) / 2, y = x;
		for (int i = 0; i < s; ++i) {
			x += dx[d], y += dy[d];
			if (x<1 || x>N || y<1 || y>N) break;
			arr[x][y] = 0;
		}

		// 2. ºóÄ­ Ã¤¿ò
		fillEmpty();

		while (1) {
			// 3. ±¸½½ Æø¹ß
			if (Bomb()) break;

			// 4. ºóÄ­ Ã¤¿ò
			fillEmpty();
		}

		// 5. ±¸½½ º¯È­
		changeBall();
	}
	cout << res[1] + res[2] * 2 + res[3] * 3;
	return 0;
}