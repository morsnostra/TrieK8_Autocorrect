package com.datastruct;
/* 
 * Struktur data Graph dengan bobot pada setiap edge
 * sources: https://www.lavivienpost.net/weighted-graph-as-adjacency-list/  
 * 
 */

import java.util.*;

class Edge<T> { 
	private T vertex; 
	private T neighbor; //connected vertex
	private int weight; //weight
	
	//Constructor, Time O(1) Space O(1)
	public Edge(T u, T v, int w) {
		this.vertex = u;
		this.neighbor = v; 
		this.weight = w;
	}

	public void setNeighbor(T neighbor) {
		this.neighbor = neighbor;
	}
	public T getNeighbor() {
		return neighbor;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getWeight() {
		return weight;
	}
	public void setVertex(T vertex) {
		this.vertex = vertex;
	}
	public T getVertex() {
		return vertex;
	}
	
	//Time O(1) Space O(1)
	@Override
	public String toString() {
		return "(" + vertex + "," + neighbor + "," + weight + ")";
	}
}

public class Graph<T> { 
    //Map<T, LinkedList<Edge<T>>> adj;
	private Map<T, MyLinearList<Edge<T>>> adj;
	boolean directed;
	
	//Constructor, Time O(1) Space O(1)
	public Graph (boolean type) { 
        adj = new HashMap<>();
		directed = type; // false: undirected, true: directed
	}

    //Add edges including adding nodes, Time O(1) Space O(1)
	public void addEdge(T a, T b, int w) {
		adj.putIfAbsent(a, new MyLinearList<>()); //add node
		adj.putIfAbsent(b, new MyLinearList<>()); //add node
		Edge<T> edge1 = new Edge<>(a, b, w);
		adj.get(a).pushQ(edge1);//add(edge1); //add edge
		if (!directed) { //undirected
			Edge<T> edge2 = new Edge<>(a, b, w);
			adj.get(b).pushQ(edge2);
		}			
	}

	//findEdgeByNodes
	private Edge<T> findEdgeByNodes(T a, T b) {
		MyLinearList<Edge<T>> edgesFromA = adj.get(a);
		if (edgesFromA == null)
			return null;
		
		Node<Edge<T>> current = edgesFromA.head;
		while (current != null) {
			Edge<T> edge = current.getData();
			if (edge.getNeighbor().equals(b)) {
				return edge;
			}
			current = current.getNext();
		}
		return null;
	}
	
	// Remove edge between two nodes
	public void removeEdge(T a, T b) {
		if (!adj.containsKey(a) || !adj.containsKey(b)) //invalid input
			return;
		MyLinearList<Edge<T>> ne1 = adj.get(a);
		MyLinearList<Edge<T>> ne2 = adj.get(b);
		if (ne1 == null || ne2 == null)
			return;
		Edge<T> edge1 = findEdgeByNodes(a, b);
		if (edge1 == null)
			return;
		ne1.remove(edge1);
		if (!directed) {//undirected
			Edge<T> edge2 = findEdgeByNodes(b, a);
			if (edge2 == null)
				return;
			ne2.remove(edge2);
		}
	}

    //Print graph as hashmap, Time O(V+E), Space O(1)
	public void printGraph() {
		for (T key: adj.keySet()) {
			//System.out.println(key.toString() + " : " + adj.get(key).toString());
            System.out.print(key.toString() + " : ");
			MyLinearList<Edge<T>> thelist = adj.get(key);
			Node<Edge<T>> curr = thelist.head;
			while(curr != null) {
				System.out.print(curr.getData());
				curr = curr.getNext();
			}
			System.out.println();
		}
	}

	//DFS
	public void DFS(T src) {
		HashMap<T, Boolean> visited = new HashMap();
		if (this.adj.containsKey(src)) {
		   visited.put(src, true);
		   Stack<T> stack = new Stack();
		   stack.push(src);
  
		   while(!stack.isEmpty()) {
			  T v = stack.pop();
			  System.out.print(String.valueOf(v) + " ");
			  MyLinearList<Edge<T>> thelist = (MyLinearList)this.adj.get(v);
  
			  for(Node<Edge<T>> curr = thelist.head; curr != null; curr = curr.getNext()) {
				 Edge<T> edge = (Edge)curr.getData();
				 T u = edge.getNeighbor();
				 if (visited.get(u) == null) {
					visited.put(u, true);
					stack.push(u);
				 }
			  }
		   }
  
		   System.out.println();
		}
	 }
  
	 //BFS
	 public void BFS(T src) {
		if (this.adj.containsKey(src)) {
		   HashMap<T, Boolean> visited = new HashMap();
		   visited.put(src, true);
		   Queue<T> queue = new LinkedList();
		   queue.add(src);
  
		   while(!queue.isEmpty()) {
			  T v = queue.poll();
			  System.out.print(String.valueOf(v) + " ");
			  MyLinearList<Edge<T>> thelist = (MyLinearList)this.adj.get(v);
  
			  for(Node<Edge<T>> curr = thelist.head; curr != null; curr = curr.getNext()) {
				 Edge<T> edge = (Edge)curr.getData();
				 T u = edge.getNeighbor();
				 if (visited.get(u) == null) {
					visited.put(u, true);
					queue.add(u);
				 }
			  }
		   }
  
		   System.out.println();
		}
	 }

	//Dijkstra Shortest Path
	public int[] shortestPath(T src, T dest) {
		if (!adj.containsKey(src) || !adj.containsKey(dest)) //invalid input
			return null;
		
		HashMap<T, Integer> vertexToIndex = new HashMap<>();
		HashMap<Integer, T> indexToVertex = new HashMap<>();
		
		int index = 0;
		for (T vertex : adj.keySet()) {
			vertexToIndex.put(vertex, index);
			indexToVertex.put(index, vertex);
			index++;
		}
		
		int n = adj.size();
		int[] shortest = new int[n];
		boolean[] visited = new boolean[n];
		int[] parents = new int[n];
	
		for (int v = 0; v < n; v++) {
			shortest[v] = Integer.MAX_VALUE;
			visited[v] = false;
			parents[v] = -1; 
		}
		
		shortest[vertexToIndex.get(src)] = 0;
		//inisialisasi jarak dari sumber/src ke vertex lain
		for (int i = 1; i < n; i++) {
			int pre = -1;
			int min = Integer.MAX_VALUE;
			
			//cari vertex yg belum dikunjungi dengan yg jarak minimum 
			for (int v = 0; v < n; v++) {
				if (!visited[v] && shortest[v] < min) {
					pre = v;
					min = shortest[v];
				}
			}
			//kalau blm ada vertex yg dikunjungi
			if (pre == -1)
				return null;
			//tandai vertex yg sudah dikunjungi
			visited[pre] = true;
			T preVertex = indexToVertex.get(pre);
			
			//update jarak ke tetangga(neighbor)
			MyLinearList<Edge<T>> edgeList = adj.get(preVertex);
			Node<Edge<T>> curr = edgeList.head;
			
			//cari tetangga dari vertex yg sudah dikunjungi
			while (curr != null) {
				Edge<T> edge = curr.getData();
				T neighbor = edge.getNeighbor();
				int neighborIndex = vertexToIndex.get(neighbor);
				int distance = edge.getWeight();

				//kalau tetangga belum dikunjungi dan jarak dari vertex yg sudah dikunjungi
				//ke tetangga lebih kecil dari jarak yg sudah ada
				//update jarak ke tetangga
				if (!visited[neighborIndex] && min != Integer.MAX_VALUE && 
					(min + distance) < shortest[neighborIndex]) {
					parents[neighborIndex] = pre;
					shortest[neighborIndex] = min + distance;
				}
				
				curr = curr.getNext();
			}
		}
		
		return new int[] {shortest[vertexToIndex.get(dest)], vertexToIndex.get(src), vertexToIndex.get(dest), parents[vertexToIndex.get(dest)]};
	}
	
	//method tambahan buat cetak path 
	public List<T> getPath(T src, T dest) {
		if (!adj.containsKey(src) || !adj.containsKey(dest)) // invalid input
			return null;
			
		HashMap<T, Integer> vertexToIndex = new HashMap<>();
		HashMap<Integer, T> indexToVertex = new HashMap<>();
		
		int index = 0;
		//buat mapping vertex ke index
		for (T vertex : adj.keySet()) {
			vertexToIndex.put(vertex, index);
			indexToVertex.put(index, vertex);
			index++;
		}
		
		int n = adj.size();
		int[] shortest = new int[n];
		boolean[] visited = new boolean[n];
		int[] parents = new int[n];
		
		for (int v = 0; v < n; v++) {
			shortest[v] = Integer.MAX_VALUE;
			visited[v] = false;
			parents[v] = -1; // NO_PARENT
		}
		
		shortest[vertexToIndex.get(src)] = 0;
		
		for (int i = 1; i < n; i++) {
			int pre = -1;
			int min = Integer.MAX_VALUE;
			
			//cari vertex yg belum dikunjungi dengan jarak minimum
			for (int v = 0; v < n; v++) {
				//kalau vertex belum dikunjungi dan jarak ke vertex lebih kecil 
				//dari jarak minimum yg sudah ada
				if (!visited[v] && shortest[v] < min) {
					pre = v;
					min = shortest[v];
				}
			}
			//kalau pre == -1 berarti semua vertex sudah dikunjungi
			//atau tidak ada vertex yg bisa dijangkau
			//return null
			if (pre == -1)
				return null;

			//tandai vertex yg sudah dikunjungi
			visited[pre] = true;
			T preVertex = indexToVertex.get(pre);
			
			//update jarak ke tetangga(neighbor)
			MyLinearList<Edge<T>> edgeList = adj.get(preVertex);
			Node<Edge<T>> curr = edgeList.head;
			
			//cari tetangga dari vertex yg sudah dikunjungi
			while (curr != null) {
				Edge<T> edge = curr.getData();
				T neighbor = edge.getNeighbor();
				int neighborIndex = vertexToIndex.get(neighbor);
				int distance = edge.getWeight();
				//kalau tetangga belum dikunjungi dan jarak dari vertex yg sudah dikunjungi
				//ke tetangga lebih kecil dari jarak yg sudah ada
				//update jarak ke tetangga
				if (!visited[neighborIndex] && min != Integer.MAX_VALUE && 
					(min + distance) < shortest[neighborIndex]) {
					parents[neighborIndex] = pre;
					shortest[neighborIndex] = min + distance;
				}
				//lanjut ke tetangga berikutnya
				curr = curr.getNext();
			}
		}
		
		int destIndex = vertexToIndex.get(dest);
		if (shortest[destIndex] == Integer.MAX_VALUE) {
			return null; //tidak ada path dari src ke dest
		}
		
		//Buat list untuk menyimpan path
		//dan tambahkan vertex tujuan ke dalam list
		List<T> path = new ArrayList<>();
		buildPath(destIndex, parents, indexToVertex, path);
		
		return path;
	}
	
	//method tambahan reukursif untuk membangun path
	//dari vertex tujuan ke vertex sumber
	//Time O(V) Space O(V)
	private void buildPath(int currentIndex, int[] parents, HashMap<Integer, T> indexToVertex, List<T> path) {
		if (currentIndex == -1) //Base case: udah sampai ke vertex sumber
			//atau tidak ada parent
			return;
		
		//ada path dari currentIndex (indeks yg sekarang) ke parent
		//maka panggil method ini lagi dengan parent sebagai currentIndex
		if (parents[currentIndex] != -1) {
			buildPath(parents[currentIndex], parents, indexToVertex, path);
		}
		
		path.add(indexToVertex.get(currentIndex));
	}

	//kruskal pake Heap sort
	public List<Edge<T>> kruskal() {
	List<Edge<T>> edges = new ArrayList<>();
	for (T vertex : adj.keySet()) {
		MyLinearList<Edge<T>> list = adj.get(vertex);
		for (Node<Edge<T>> cur = list.head; cur != null; cur = cur.getNext()) {
			edges.add(cur.getData());
		}
	}
	//nyortir edges berdasarkan weight,bandingin pakai comparator
	Collections.sort(edges, Comparator.comparingInt(Edge::getWeight));
	
	//inisialisasi MST
	List<Edge<T>> mst = new ArrayList<>();
	//set untuk menyimpan vertex yang sudah dikunjungi
	//agar tidak ada edge yang sama
	Set<T> visited = new HashSet<>();
	
	//iterasi edges
	
	for (Edge<T> edge : edges) {
		T u = edge.getVertex();
		T v = edge.getNeighbor();

		//kalau vertex belum ada di MST, tambahkan ke MST
		if (!visited.contains(u) || !visited.contains(v)) {
			mst.add(edge);
			visited.add(u);
			visited.add(v);
		}
	}
	return mst;
}

	//print Kruskal MST beserta MST length
	//Time O(E log E) Space O(E)
	public void printKruskalMST() {
		List<Edge<T>> mst = kruskal();
		int jmlBobot_W = 0;
		for (Edge<T> edge : mst) {
			System.out.println("[" + edge + "]");
			jmlBobot_W += edge.getWeight();
		}
		System.out.println("MST Length: " + jmlBobot_W);
	}

	//Topological Ordering dari DAG
	public List<T> topologicalOrdering() {
		if (!directed) {
			System.out.println("Topological ordering hanya bisa dilakukan pada directed graph");
			return null;
		}
		
		HashMap<T, Integer> indegree = new HashMap<>(); //utk menyimpan indegree setiap vertex, <T= vertex, Integer= jumlah indegree>
		
		//inisialisasi indegree semua vertex 
		for (T vertex : adj.keySet()) { 	//ambil semua key dr adj, simpan ke vertex, jalan sebanyak jumlah vertex
											// keySet() => method hashMap untuk mengembalikan set yg berisi semua key
			indegree.put(vertex, 0);	//awalnya semua indegreenya 0
		}
		
		//hitung indegree setiap vertex
		for (T vertex : adj.keySet()) {
			MyLinearList<Edge<T>> edgeList = adj.get(vertex); 	//MyLinearList yang berisi list edge kluar/outdegree dari vertex
			Node<Edge<T>> curr = edgeList.head; 				//mulai dari head (node pertama)
			
			while (curr != null) {
				Edge<T> edge = curr.getData();		//ambil data edge dr node curr
				T neighbor = edge.getNeighbor();	//ambil vertex yg dituju oleh edge 
				
				indegree.put(neighbor, indegree.get(neighbor) + 1);	//update nilai indegree di hashmap
				curr = curr.getNext(); 								//pindah ke node selanjutnya di linked list
			}
		}
		
		//bikin queue utk simpan vertex yang indegreenya 0
		MyLinearList<T> queue = new MyLinearList<>(); //menyimpan vertex yang mau diproses (yg indegreenya 0)
		
		for (T vertex : indegree.keySet()) {	//cari semua vertex yang indegreenya 0 
			if (indegree.get(vertex) == 0) {	
				queue.pushQ(vertex);			//dan masukkan ke queue
			}
		}
		
		//bikin list utk simpan hasilnya
		List<T> result = new ArrayList<>();	//arraylist (implementasi list)

		//topological sort
		while (!queue.isEmpty()) {

			T current = queue.remove(); //ambil vertex dari queue (dequeue)
			result.add(current);		//dan tambahkan ke hasil topological orderingnya
			
			//update indegree neighbour
			MyLinearList<Edge<T>> edgeList = adj.get(current);	//ambil outdegree current vertex
			Node<Edge<T>> curr = edgeList.head; 				//mulai dr head
			
			while (curr != null) {
				Edge<T> edge = curr.getData();		//ambil data edge
				T neighbor = edge.getNeighbor();	//ambil neighbour
				
				//kurangi indegree neighbour
				indegree.put(neighbor, indegree.get(neighbor) - 1);
				
				//kalo indegree neighbor jadi 0 abis dikurangin, masuk ke queue
				if (indegree.get(neighbor) == 0) {
					queue.pushQ(neighbor);
				}
				
				curr = curr.getNext(); //pindah ke edge selanjutnnya
			}
		}
		
		//cek apakah ada cycle
		//result.size() => total vertex yang uda berhasil diproses
        //adj.size() => total vertex di graph
		if (result.size() != adj.size()) {
			System.out.println("tidak bisa melakukan topological ordering karena graph mempunyai cycle");
			return null;
		}
		
		return result; //return hasil topological ordering
	}
	
	public void printTopologicalOrdering() {
		List<T> ordering = topologicalOrdering(); //panggil method topologicalOrdering() dan simpan hasilnya ke ordering
		
		if (ordering != null) {
			System.out.print("Topological Ordering: [");
			
			for (int i = 0; i < ordering.size(); i++) {
				System.out.print(ordering.get(i));
				
				if (i < ordering.size() - 1) {
					System.out.print("");
				}
			}

			System.out.println("]");
		}
	}
}
