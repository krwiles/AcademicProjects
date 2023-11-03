# Hash table with chaining.
class HashTable:
    # Initialize hash table with a table and empty buckets.
    def __init__(self, table_size=17):
        self.table = []
        for i in range(table_size):
            self.table.append([])

    # Insert a new item into the hash table as a key value tuple.
    def insert(self, key, item):
        bucket = hash(key) % len(self.table)
        chain = self.table[bucket]
        pair = (key, item)

        # Update current value if key already exists.
        for i in range(len(chain)):
            if chain[i][0] == key:
                chain[i] = pair
                return True

        chain.append(pair)
        return True

    # Remove a key value tuple from the hash table.
    def remove(self, key):
        bucket = hash(key) % len(self.table)
        chain = self.table[bucket]

        for i in range(len(chain)):
            if chain[i][0] == key:
                del chain[i]
                return True

        return False

    # Search the hash table with a key and return the value.
    def search(self, key):
        bucket = hash(key) % len(self.table)
        chain = self.table[bucket]

        for i in range(len(chain)):
            if chain[i][0] == key:
                return chain[i][1]

        return False
