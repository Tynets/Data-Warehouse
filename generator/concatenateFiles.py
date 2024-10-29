import os

filePathT1 = "generator/DataT1";
filePathT2 = "generator/DataT2";
dataT1 = os.listdir(filePathT1);
dataT2 = os.listdir(filePathT2);
for i in range(len(dataT1)):
    fileT1 = open(filePathT1 + "/" + dataT1[i], "r");
    fileT2 = open(filePathT2 + "/" + dataT2[i], "r+");
    contentT1 = fileT1.read();
    contentT2 = fileT2.read();
    fileT2.seek(0, 0);
    fileT2.write(contentT2 + contentT1);