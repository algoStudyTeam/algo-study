#include <iostream>
#include <vector>
#include <climits>
#include <cmath>
#include <cstdlib>
using namespace std;
struct info {
	int x, y;
	info() {}
	info(int x, int y) { this->x = x; this->y = y; }
};
int N, M, res = INT_MAX;
int arr[51][51];
vector<info> chicken, house;
vector<int> selected;
void combination(int n) {
	if (selected.size() == M) {
		int sum = 0;
		for (int i = 0; i < house.size(); ++i) {
			int Min = INT_MAX;
			for (int j = 0; j < selected.size(); ++j) Min = min(Min, abs(house[i].x - chicken[selected[j]].x) + abs(house[i].y - chicken[selected[j]].y));
			sum += Min;
		}
		res = min(res, sum);
		return;
	}
	for (int i = n; i < chicken.size(); ++i) {
		selected.push_back(i);
		combination(i + 1);
		selected.pop_back();
	}
}
int main() {
	cin >> N >> M;
	for (int i = 0; i < N; ++i) {
		for (int j = 0; j < N; ++j) {
			cin >> arr[i][j];
			if (arr[i][j] == 2)chicken.push_back({ i,j });
			else if (arr[i][j] == 1)house.push_back({ i,j });
		}
	}
	combination(0);
	cout << res;
	return 0;
}
