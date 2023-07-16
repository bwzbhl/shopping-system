
with open('name_list.csv', 'r') as f:
    lines = f.readlines()
    count = len(lines)

import random

one = random.choice(lines)
two = random.choice(lines)
three = random.choice(lines)

test_set = set()
test_set.add(one)
test_set.add(two)
test_set.add(three)

if len(test_set) == 3:
    print('三个同学的名字皆不相同。')

    print(f'第一个同学是 {one.strip()}')
    print(f'第一个同学是 {two.strip()}')
    print(f'第一个同学是 {three.strip()}')
else:
    print('有相同的名字，重新来吧')
