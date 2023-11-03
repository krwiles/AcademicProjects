from datetime import *


# Package class to hold all individual package data.
class Package:
    def __init__(self, id_num, address, city, state, zip_code, deadline, weight, notes):
        self.id_num = id_num
        self.address = address
        self.city = city
        self.state = state
        self.zip_code = zip_code
        self.deadline = deadline
        self.weight = weight
        self.notes = notes
        self.delayed_arrival = datetime.strptime('8:00 AM', '%I:%M %p')

        # Set status to delayed if indicated by notes.
        if notes.find('Delayed') != -1 or notes.find('Wrong') != -1:
            self.status = 'Delayed'
        else:
            self.status = 'At hub'

        # If package is delayed from notes set the delayed arrival.
        if notes.find('Delayed on flight') != -1:
            self.delayed_arrival = datetime.strptime(notes[51:], '%I:%M %p')

        self.group = set()
        self.delivered_time = None
        self.truck_id = 'Pending'

    # Add a package to the group, indicating they must be delivered together.
    def group_with(self, package):
        self.group.add(package)
