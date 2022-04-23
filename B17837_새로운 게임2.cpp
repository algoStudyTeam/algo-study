#include <iostream>
#include <deque>
using namespace std;
struct info {
	int x, y, d;
	info() {}
	info(int x, int y, int d) { this->x = x; this->y = y; this->d = d; }
};

int N, K, res = -1;
int arr[13][13];
int dx[5] = { 0,0,0,-1,1 }, dy[5] = { 0,1,-1,0,0 }, dd[5] = { 0,2,1,4,3 }; // ���»���
deque<int>chess[13][13];
info loc[11]; // �� ��ġ
int main() {
	cin >> N >> K;
	for (int i = 0; i < N; ++i) for (int j = 0; j < N; ++j) cin >> arr[i][j]; // 0:��, 1:��, 2:��
	for (int i = 1; i <= K; ++i) {
		int a, b, c;
		cin >> a >> b >> c;
		chess[a - 1][b - 1].push_back(i);
		loc[i] = { a - 1,b - 1,c };
	}
	for (int t = 1; t <= 1000; ++t) {
		for (int n = 1; n <= K; ++n) {
			int x = loc[n].x + dx[loc[n].d], y = loc[n].y + dy[loc[n].d];
			if (x < 0 || x >= N || y < 0 || y >= N || arr[x][y] == 2) { // ü������ ����ų� �Ķ����� ���
				x -= dx[loc[n].d], y -= dy[loc[n].d];
				loc[n].d = dd[loc[n].d]; // ���� ��ȯ
				x += dx[loc[n].d], y += dy[loc[n].d];
				if (x < 0 || x >= N || y < 0 || y >= N || arr[x][y] == 2) continue; // ���� �ٲ㵵 ü������ ����ų� �Ķ����� ���� ������ ����
			}
			deque<int>tmp;
			while (!chess[loc[n].x][loc[n].y].empty()) {
				tmp.push_back(chess[loc[n].x][loc[n].y].back()); // �ڿ������� n���� ���� ������ ��
				chess[loc[n].x][loc[n].y].pop_back();
				if (tmp.back() == n) break;
			}
			if (arr[x][y] == 0) { // ����� ���
				while (!tmp.empty()) {
					loc[tmp.back()].x = x, loc[tmp.back()].y = y; // �ڿ������� �����鼭
					chess[x][y].push_back(tmp.back()); // �ڿ� ����
					tmp.pop_back();
				}
			}
			else if (arr[x][y] == 1) { // �������� ���
				while (!tmp.empty()) {
					loc[tmp.front()].x = x, loc[tmp.front()].y = y; // �տ������� �����鼭
					chess[x][y].push_back(tmp.front()); // �ڿ� ����
					tmp.pop_front();
				}
			}
			if (chess[x][y].size() >= 4) {
				res = t;
				break;
			}
		}
		if (res != -1) break;
	}
	cout << res;
	return 0;
}