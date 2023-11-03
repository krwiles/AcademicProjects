import csv
from datetime import *
from package import Package
from hashtable import HashTable
from truck import Truck


# Function to load the distance table into a matrix and the addresses into a list.
def load_map_data(csv_name):
    map_matrix = []

    with open(csv_name) as f:
        reader = csv.reader(f)
        addresses = next(reader)[1:]
        for row in reader:
            map_matrix.append(row[1:])

    return map_matrix, addresses


# Function to load the packages file and put them into a hash table.
def load_package_table(csv_name):
    table = HashTable()
    id_list = []  # A list to hold the id's of packages to be delivered.

    with open(csv_name) as f:
        reader = csv.reader(f)
        for row in reader:
            deadline = timestr_parse(row[5])
            package = Package(int(row[0]), row[1], row[2], row[3], int(row[4]), deadline, int(row[6]), row[7])
            table.insert(package.id_num, package)
            id_list.append(package.id_num)

    # Find packages that should be delivered in a group and add them to sets in the package objects.
    for e in id_list:
        package = table.search(e)
        if package.notes.find('Must be delivered with') != -1:
            group = package.notes[23:].split(', ')
            for p in group:
                package.group_with(table.search(int(p)))
                table.search(int(p)).group_with(package)

    return table, id_list


# Function to parse a time string into a datetime object.
def timestr_parse(time_string):
    if time_string == 'EOD':
        return datetime.strptime('5:00 PM', '%I:%M %p')
    else:
        return datetime.strptime(time_string, '%I:%M %p')


# Function updates correct information for package 9.
def update_package_9(package_table):
    p9 = package_table.search(9)
    p9.address = '410 S State St'
    p9.city = 'Salt Lake City'
    p9.zip_code = '84111'


# Recursive DFS algorithm to find all packages that need to be delivered together.
def delivery_group(package, delivery_list):  # Input a list to fill with the packages.
    if package not in delivery_list:
        delivery_list.append(package)
        for adj_package in package.group:
            delivery_group(adj_package, delivery_list)


# Algorithm to load packages on a truck while accounting for constraints.
def truck_load_packages(truck, load_order, package_table):
    i = 0
    while not truck.is_full() and i < len(load_order):
        package = package_table.search(load_order[i])

        # Check if the package must be on truck 2.
        if package.notes.find('Can only be on truck 2') != -1 and truck.id_num != 2:
            i += 1

        # Check if the package must be delivered with other packages.
        elif len(package.group) > 0:
            delivery_list = []
            delivery_group(package, delivery_list)

            # Check that the truck has room for all in the group.
            if truck.available_space() >= len(delivery_list):
                for pack in delivery_list:
                    truck.load(pack)
                    load_order.remove(pack.id_num)
            else:
                i += 1

        # Check if the package is currently at the hub (not delayed).
        elif package.status == 'At hub' or truck.time >= package.delayed_arrival:
            truck.load(package)
            load_order.pop(i)

        # Else skip over the package.
        else:
            i += 1


# Function to give the distance between two addresses.
def address_distance(address1, address2, map_matrix, addresses):
    index1 = 0
    index2 = 0

    # Find index values for both addresses.
    for i in range(len(addresses)):
        if addresses[i].find(address1) != -1:
            index1 = i
        if addresses[i].find(address2) != -1:
            index2 = i

    # Because the matrix is symmetrical and only half full, it must always access the bottom half.
    if index1 > index2:
        return float(map_matrix[index1][index2])
    else:
        return float(map_matrix[index2][index1])


# Function to find the nearest address of all packages in a truck, and the distance to it.
def find_nearest_address(truck, map_matrix, addresses):
    nearest_add = ''
    nearest_dist = float('inf')
    for package in truck.cargo:
        if package.address.find(truck.location) == -1:  # Make sure it doesn't get stuck picking the current address.
            dist = address_distance(truck.location, package.address, map_matrix, addresses)
            if dist < nearest_dist:
                nearest_add = package.address
                nearest_dist = dist

    return nearest_add, nearest_dist


# Algorithm to have a truck deliver its packages (nearest neighbor algorithm).
def truck_deliver(truck, map_matrix, addresses, stop_time):
    # Use nearest neighbor algorithm to select the next destination for the truck and deliver packages.
    while len(truck.cargo) > 0:

        # Check if any packages are very close to their deadline and need priority.
        if truck.urgent_package_check():
            destination = truck.urgent_package.address
            distance = address_distance(truck.location, destination, map_matrix, addresses)
        # Else find the nearest address to deliver to.
        else:
            destination, distance = find_nearest_address(truck, map_matrix, addresses)

        # Travel to the destination.
        truck.travel(destination, distance)

        # Check time against desired stop time from user input.
        if truck.time >= stop_time:
            truck.rolling = False
            return

        # Deliver packages at the current location.
        truck.deliver_at_location()

    # Return to the hub once the cargo is all delivered.
    distance = address_distance(truck.location, addresses[0], map_matrix, addresses)
    truck.travel('At hub', distance)

    # Check final time against desired stop time from user input.
    if truck.time >= stop_time:
        truck.rolling = False


# This function loads a truck and delivers its packages.
def load_and_deliver(truck, package_table, load_order, map_matrix, addresses, stop_time):
    # Check time for package 9 update.
    if truck.time >= package_table.search(9).delayed_arrival:
        update_package_9(package_table)

    # Load packages on the truck from the hub.
    truck_load_packages(truck, load_order, package_table)

    # If only delayed packages are left, have the truck wait until the earliest one is ready.
    if len(truck.cargo) == 0:
        delay_until = datetime.strptime('11:59 PM', '%I:%M %p')
        for id_num in load_order:
            if package_table.search(id_num).delayed_arrival < delay_until:
                delay_until = package_table.search(id_num).delayed_arrival

        truck.time = delay_until
        truck_load_packages(truck, load_order, package_table)

    # Deliver the packages on the truck.
    truck_deliver(truck, map_matrix, addresses, stop_time)


# This is the main function that will run the trucks until the stop time is reached.
def main_function(stop_time):
    package_table, id_list = load_package_table('Package File.csv')
    map_matrix, addresses = load_map_data('Distance Table.csv')

    # Manually setting package 9 with a delayed arrival due to incorrect address.
    package_table.search(9).delayed_arrival = datetime.strptime('10:20 AM', '%I:%M %p')

    # Create a load order list and sort by deadline.
    # This is how packages are ordered to be loaded into the trucks.
    load_order = id_list[::]
    for i in range(len(load_order)):
        j = i
        while j > 0 and package_table.search(load_order[j]).deadline < package_table.search(load_order[j - 1]).deadline:
            temp = load_order[j]
            load_order[j] = load_order[j - 1]
            load_order[j - 1] = temp
            j -= 1

    truck_1 = Truck(1)
    truck_2 = Truck(2)

    # Main loop for trucks:
    # Calling load_and_deliver for each truck as they return to the hub until the packages are all delivered or
    # the stop time has been reached.
    while len(load_order) > 0 and (truck_1.rolling or truck_2.rolling):
        # Ensure trucks are loaded chronologically to make full use of time.
        if truck_1.time <= truck_2.time:
            load_and_deliver(truck_1, package_table, load_order, map_matrix, addresses, stop_time)
        else:
            load_and_deliver(truck_2, package_table, load_order, map_matrix, addresses, stop_time)

    return package_table, id_list, truck_1, truck_2


# Function to print a line for a package with all of its information.
def print_package_line(package):
    if package.delivered_time is None:
        delivered = 'Pending '
    else:
        delivered = package.delivered_time.time()

    print(
        f'| {package.id_num:2}',
        f'| {package.address:38}',
        f'| {package.city:16}',
        f'| {package.state:5}',
        f'| {package.zip_code:5}',
        f'| {package.weight:6}',
        f'| {package.truck_id:>7}',
        f'| {package.status:20}',
        f'| {package.deadline.time()}',
        f'| {delivered}     ',
        f'| {package.notes}',
    )


# Function to print the header that goes with the package lines.
def print_header():
    print(
        '| {:^2}'.format('ID'),
        '| {:^38}'.format('Address'),
        '| {:^16}'.format('City'),
        '| {:^5}'.format('State'),
        '| {:^5}'.format('Zip'),
        '| {:^6}'.format('Weight'),
        '| {:^7}'.format('Truck'),
        '| {:^20}'.format('Status'),
        '| {:^8}'.format('Deadline'),
        '| {:^13}'.format('Delivery Time'),
        '| Notes',
    )


# Command line interface block.
if __name__ == '__main__':
    day_end = timestr_parse('5:00 PM')
    package_table, id_list, truck_1, truck_2 = main_function(day_end)

    print('TRUCK DELIVERY SYSTEM\n')
    print('Deliveries complete!')
    print('Truck 1 mileage:', '{:.1f}'.format(truck_1.travel_distance))
    print('Truck 2 mileage:', '{:.1f}'.format(truck_2.travel_distance))
    print('Total truck mileage:', '{:.1f}'.format(truck_1.travel_distance + truck_2.travel_distance), '\n')

    # Main UI loop.
    end_program = False
    while not end_program:
        print('Options:')
        print('1. Package status by ID')
        print('2. All package status\'')
        print('3. Select new time')
        print('4. Exit')
        option = input('Select option (1, 2, 3, or 4): ')

        # Print status of a single package.
        if option == '1':
            try:
                id_num = int(input('Enter package ID: '))
            except ValueError:
                print('\nERROR: enter a number for package ID\n')
                continue

            package = package_table.search(id_num)

            if not package:
                print('\nERROR: package ID not found\n')
                continue

            print('\n')
            print_header()
            print_package_line(package)
            print('\n')

        # Print status of all packages and truck mileage.
        elif option == '2':
            print('\n')
            print_header()
            for id_num in id_list:
                package = package_table.search(id_num)
                print_package_line(package)

            print('\nTruck 1 mileage:', '{:.1f}'.format(truck_1.travel_distance))
            print('Truck 2 mileage:', '{:.1f}'.format(truck_2.travel_distance))
            print('Total truck mileage:', '{:.1f}'.format(truck_1.travel_distance + truck_2.travel_distance), '\n')

        # Change the stop time for the main program to see status at a specified time.
        elif option == '3':
            try:
                stop_time = timestr_parse(input('Enter new time (format like: 5:00 PM): '))
            except ValueError:
                print('\nERROR: incorrect time format\n')
                continue

            package_table, id_list, truck_1, truck_2 = main_function(stop_time)

            # Update delayed package status manually (trucks will not update packages until they return to the hub).
            for id_num in id_list:
                package = package_table.search(id_num)
                if package.status == 'Delayed' and stop_time >= package.delayed_arrival:
                    package.status = 'At hub'

            print('\nNew time set to', stop_time.time(), '\n')

        # End the program.
        elif option == '4':
            end_program = True

        else:
            print('\nERROR: no option selected\n')
