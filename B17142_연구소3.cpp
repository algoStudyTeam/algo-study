#include <iostream>
#include <vector>
#include <queue>
using namespace std;
int N, M, x, y, place = 0, res = 10000000;
int arr[51][51], dx[4] = { -1, 1, 0, 0 }, dy[4] = { 0, 0, -1, 1 };
vector <pair<int, int>> virus, active;
void Bfs() {
	int cnt = 0, sec = 0;
	bool visited[51][51] = { false };
	queue <pair<int, int>> q, tmp;
	for (int i = 0; i < M; i++) q.push(make_pair(active[i].first, active[i].second)), visited[active[i].first][active[i].second] = true;
	while (1) {
		while (!q.empty()) {
			for (int i = 0; i < 4; i++) {
				int x = q.front().first + dx[i], y = q.front().second + dy[i];
				if (x >= 0 && x < N && y >= 0 && y < N && (arr[x][y] == 2 || arr[x][y] == 0) && !visited[x][y] && cnt < place) {
					if (!arr[x][y]) cnt++;
					visited[x][y] = true;
					tmp.push(make_pair(x, y));
				}
			}
			q.pop();
			if (place == cnt) {
				if (!tmp.empty()) sec++;
				res = min(res, sec);
				return;
			}
		}
		if (!tmp.empty()) sec++;
		if (tmp.empty() || sec > res) return;
		while (!tmp.empty()) {
			q.push(make_pair(tmp.front().first, tmp.front().second));
			tmp.pop();
		}
	}
}
void Combination(int idx, int cnt) { //활성화 시킬 M개의 바이러스를 고름
	if (cnt == M) {
		Bfs();
		return;
	}
	for (int i = idx; i < virus.size(); i++) {
		active.push_back(make_pair(virus[i].first, virus[i].second));
		Combination(i + 1, cnt + 1);
		active.pop_back();
	}
}
int main() {
	cin >> N >> M;
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			cin >> arr[i][j];
			if (!arr[i][j]) place++; //빈칸 개수 카운트
			else if (arr[i][j] == 2) virus.push_back(make_pair(i, j)); //바이러스 위치 저장
		}
	}
	Combination(0, 0);
	if (res == 10000000) cout << -1;
	else cout << res;
	return 0;
}