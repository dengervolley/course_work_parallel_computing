import requests
import fnmatch
import os
api_url = "http://localhost:8080/api/file";
var_number = 37 # 34 + 7
sizes = [12500]
start_indexes = []
end_indexes = []
dir_prefix = 'datasets/aclImdb';
file_dirs = [f'{dir_prefix}/test/neg', f'{dir_prefix}/test/pos', f'{dir_prefix}/train/neg', f'{dir_prefix}/train/pos']
log_file = open("upload-log.txt", "a")

for size in sizes:
    start_index = (size / 50) * (var_number - 1)
    end_index = start_index + 2 # (size / 50) * var_number
    start_indexes.append(start_index)
    end_indexes.append(end_index) 

for file_dir in file_dirs:
    for file in os.listdir(file_dir):
        for start_index, stop_index in zip(start_indexes, end_indexes):
            index_range = [str(i) for i in range(int(start_index), int(stop_index))]
            if file.startswith(tuple(index_range)):
                stream = open(f'{file_dir}/{file}', "rb")
                response = requests.post(api_url, files = {"file": stream})
                if response.ok:
                    print(f'{file} uploaded successfully')
                    #log_file.write(f'{file} uploaded successfully\n')
                else:
                    print(f'an error occured: {response.text}')
                    #log_file.write(f'an error occured: {response.text}')

                    

