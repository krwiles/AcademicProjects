from datetime import *


# Truck class to represent the trucks delivering packages.
class Truck:
    def __init__(self, id_num, capacity=16, speed=18):
        self.id_num = id_num
        self.capacity = capacity
        self.speed = speed
        self.cargo = []
        self.location = 'At hub'
        self.time = datetime.strptime('8:00 AM', '%I:%M %p')
        self.travel_distance = 0
        self.urgent_package = None
        self.rolling = True  # Bool representing if a truck is delivering or if the desired stop time has occurred.

    # Load a package onto the truck.
    def load(self, package):
        if len(self.cargo) < self.capacity:
            package.status = f'On truck {self.id_num}'
            package.truck_id = f'{self.id_num}'
            self.cargo.append(package)
            return True
        return False

    # Check if the cargo has reached full capacity.
    def is_full(self):
        if len(self.cargo) == self.capacity:
            return True
        else:
            return False

    # Return how much empty space is left in cargo.
    def available_space(self):
        return self.capacity - len(self.cargo)

    # Have the truck travel to a new destination
    def travel(self, destination, distance):
        t = distance / self.speed
        self.time += timedelta(hours=t)
        self.location = destination
        self.travel_distance += distance

    # Deliver packages in cargo that are addressed to the current location.
    def deliver_at_location(self):
        delivered = []

        for package in self.cargo:
            if package.address == self.location:
                package.status = f'Delivered by truck {self.id_num}'
                package.delivered_time = self.time
                delivered.append(package)

        for package in delivered:
            self.cargo.remove(package)

    # Check if any package is close to its deadline.
    def urgent_package_check(self):
        remaining_time = timedelta(hours=24)
        urgent_package = None

        for package in self.cargo:
            t = package.deadline - self.time
            if t < remaining_time:
                remaining_time = t
                urgent_package = package

        if remaining_time < timedelta(minutes=30):
            self.urgent_package = urgent_package
            return True
        else:
            return False
