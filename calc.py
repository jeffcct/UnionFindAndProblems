class Node:
    def __init__(self, name):
        self.name = str(name)
        self.neighbours = []
    
    def addNeighbour(self, other): #type: ignore
        self.neighbours.append(other)
        other.neighbours.append(self)

    def __str__(self):
        return self.name
    
    def __repr__(self):
        return self.name

class UnionSet:
    def __init__(self, components: dict[int, set] | int):
        self.nodeAddress = {}
        if type(components) == dict:
            self.connections: dict = components
            for name in components.keys():
                for node in components[name]:
                    self.nodeAddress[node] = name
        elif type(components) == int:
            self.connections = {}
            for i in range(components):
                self.connections[i] = set([i])
                self.nodeAddress[i] = i


    def isConnected(self, node1, node2):
        return self.nodeAddress[node1] == self.nodeAddress[node2]
    
    def union(self, node1, node2):
        if self.isConnected(node1, node2):
            return
    
        keepAddress = self.nodeAddress[node1]
        removeAddress = self.nodeAddress[node2]
        for node in self.connections[removeAddress]:
            self.nodeAddress[node] = keepAddress
            self.connections[keepAddress].add(node)
        del self.connections[removeAddress]

    def __str__(self):
        return f"Addresses: {self.nodeAddress}\nConnections: {self.connections}"

class QuickUnionSet:
    def __init__(self, number):
        self.parents = [i for i in range(number)]
        self.length = [1 for i in range(number)]

    def getFinalParent(self, parent):
        while self.parents[parent] != parent:
            nodes.append(parent)
            parent = self.parents[parent]
        return parent

    def isConnected(self, node1, node2):
        parent1 = self.getFinalParent(node1)
        parent2 = self.getFinalParent(node2)
        return parent1 == parent2
    
    def union(self, node1, node2):
        parent1 = self.getFinalParent(node1)
        parent2 = self.getFinalParent(node2)
        if parent1 != parent2:
            if self.length[parent1] >= self.length[parent2]:
                self.parents[parent2] = parent1
                self.length[parent2] += self.length[parent1]
            else:
                self.parents[parent1] = parent2
                self.length[parent1] += self.length[parent2]

    def connectedComponent(self):
        components = {}
        for i in range(len(self.parents)):
            parent = self.getFinalParent(i)
            components[parent] = components.get(parent, []) + [i]
        return components
    
    def __str__(self):
        return f"Parent Addresses: {self.parents}\nComponents: {self.connectedComponent()}"
    
    def __repr__(self):
        return f"Parent Addresses: {self.parents}\nComponents: {self.connectedComponent()}"


class QuickUnionSetWithCompression:
    def __init__(self, number):
        self.parents = [i for i in range(number)]
        self.length = [1 for i in range(number)]

    def getFinalParent(self, parent):
        nodes = []
        while self.parents[parent] != parent:
            nodes.append(parent)
            parent = self.parents[parent]
        for node in nodes:
            self.parents[node] = parent
        return parent

    def isConnected(self, node1, node2):
        parent1 = self.getFinalParent(node1)
        parent2 = self.getFinalParent(node2)
        return parent1 == parent2
    
    def union(self, node1, node2):
        parent1 = self.getFinalParent(node1)
        parent2 = self.getFinalParent(node2)
        if parent1 != parent2:
            if self.length[parent1] >= self.length[parent2]:
                self.parents[parent2] = parent1
                self.length[parent2] += self.length[parent1]
            else:
                self.parents[parent1] = parent2
                self.length[parent1] += self.length[parent2]
    
    def connectedComponent(self):
        components = {}
        for i in range(len(self.parents)):
            parent = self.getFinalParent(i)
            components[parent] = components.get(parent, []) + [i]
        return components
    
    def __str__(self):
        return f"Parent Addresses: {self.parents}\nComponents: {self.connectedComponent()}"
    
    def __repr__(self):
        return f"Parent Addresses: {self.parents}\nComponents: {self.connectedComponent()}"
        



def connectedComponents(values) -> dict[int, set]:
    seen = set()
    connected = {}
    name = 1
    for value in values:
        if value not in seen:
            connected[name] = dfsConnected(value, seen)
            name += 1
    return connected

def dfsConnected(node, seen):
    connectedSet = set()
    stack = [node]

    while stack != []:
        item = stack.pop()
        seen.add(item)
        connectedSet.add(item)
        for neighbour in item.neighbours:
            if neighbour not in connectedSet:
                stack.append(neighbour)
    return connectedSet


nodes = [Node(i) for i in range(10)]
components = connectedComponents(nodes)

out = UnionSet(components)
out.union(nodes[4], nodes[3])
out.union(nodes[3], nodes[8])
out.union(nodes[6], nodes[5])
out.union(nodes[9], nodes[4])
out.union(nodes[2], nodes[1])
print(out.isConnected(nodes[0], nodes[7]))
print(out.isConnected(nodes[8], nodes[9]))
out.union(nodes[5], nodes[0])
out.union(nodes[7], nodes[2])
out.union(nodes[6], nodes[0])
out.union(nodes[1], nodes[0])
print(out.isConnected(nodes[0], nodes[7]))
print(out)


out = QuickUnionSetWithCompression(10)
out.union(4, 3)
out.union(3, 8)
out.union(6, 5)
out.union(9, 4)
out.union(2, 1)
print(out.isConnected(0, 7))
print(out.isConnected(8, 9))
out.union(5, 0)
out.union(7, 2)
out.union(6, 0)
out.union(1, 0)
print(out.isConnected(0, 7))
print(out)


        
