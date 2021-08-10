
def fall(path):
    import cv2
    import time

    fitToEllipse = False
    cap = cv2.VideoCapture(path)


    fgbg = cv2.createBackgroundSubtractorMOG2()
    j = 0
    flag=0
    while (cap.isOpened()):
        ret, frame = cap.read()

        if(ret==False):
            break

        #Convert each frame to gray scale and subtract the background
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        fgmask = fgbg.apply(gray)

        #Find contours
        contours, _ = cv2.findContours(fgmask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

        if contours:

            # List to hold all areas
            areas = []

            for contour in contours:
                ar = cv2.contourArea(contour)
                areas.append(ar)

            max_area = max(areas, default = 0)

            max_area_index = areas.index(max_area)

            cnt = contours[max_area_index]

            M = cv2.moments(cnt)

            x, y, w, h = cv2.boundingRect(cnt)

            cv2.drawContours(fgmask, [cnt], 0, (255,255,255), 3, maxLevel = 0)

            if h < w:
                j += 1

            if j > 10:
                #print("yo")
                flag=1
                cv2.putText(frame, f"FALL",(30, 50), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 2,cv2.LINE_AA)
                cv2.rectangle(frame,(x,y),(x+w,y+h),(0,0,255),2)

            if h > w:
               # print("p")
                j = 0
                cv2.rectangle(frame,(x,y),(x+w,y+h),(0,255,0),2)




    cap.release()
    if(flag ==1 ):
        return "Yes"
    else:
        return "No"
        """

    import cv2
    import numpy as np
    import math
    import time
    cap = cv2.VideoCapture(path)
    count = 0
    count1 =0
    slope=0
    slope1 = 100
    minArea = 120*100
    radianToDegree=57.324
    minimumLengthOfLine=150.0
    minAngle=18
    maxAngle=72
    list_falls=[]
    count_fall=0
    firstFrame= None

   # time.sleep(1)

    #Function definition for frame Conversion
    def convertFrame(frame):
        r = 750.0 / frame.shape[1]
        dim = (750, int(frame.shape[0] * r))
        frame = cv2.resize(frame, dim, interpolation=cv2.INTER_AREA)
        gray = cv2.cvtColor(frame,cv2.COLOR_BGR2GRAY)
        gray = cv2.GaussianBlur(gray, (31,31),0)

        return frame,gray

    while (cap.isOpened()):

        ret,frame= cap.read()
        if frame is None:
            break
        if (ret==False):
            break
        frame,gray = convertFrame(frame);

        #comparison Frame
        if firstFrame is None:
           # time.sleep(1.0)
            _,frame= cap.read()
            frame,gray=convertFrame(frame)
            firstFrame = gray
            continue

        #Frame difference between current and comparison frame
        frameDelta= cv2.absdiff(firstFrame,gray)
        #Thresholding
        thresh1 = cv2.threshold(frameDelta,20,255,cv2.THRESH_BINARY)[1]
        #Dilation of Pixels
        thresh = cv2.dilate(thresh1,None,iterations = 15)



        #Finding the Region of Interest with changes
        contour,_ = cv2.findContours(thresh.copy(),cv2.RETR_EXTERNAL,cv2.CHAIN_APPROX_SIMPLE)

        for con in contour:

            if len(con)>=5 and cv2.contourArea(con)>minArea:
                ellipse = cv2.fitEllipse(con)
                cv2.ellipse(frame,ellipse,(255,255,0),5)

                #Co-ordinates of extreme points
                extTop = tuple(con[con[:, :, 1].argmin()][0])
                extBot = tuple(con[con[:, :, 1].argmax()][0])
                extLeft = tuple(con[con[:, :, 0].argmin()][0])
                extRight = tuple(con[con[:, :, 0].argmax()][0])

                line1 = math.sqrt((extTop[0]-extBot[0])(extTop[0]-extBot[0])+(extTop[1]-extBot[1])(extTop[1]-extBot[1]))
                midPoint = [extTop[0]-int((extTop[0]-extBot[0])/2),extTop[1]-int((extTop[1]-extBot[1])/2)]
                if line1>minimumLengthOfLine:
                    #cv2.line(frame,(extBot[0],extBot[1]),(extTop[0],extTop[1]), (255, 0, 0), 5)
                    if (extTop[0]!=extBot[0]):
                        slope = abs(extTop[1]-extBot[1])/(extTop[0]-extBot[0])

                else:
                    #cv2.line(frame, (extLeft[0], extLeft[1]), (extRight[0], extRight[1]), (255, 0, 0), 5)
                    if (extRight[0] != extLeft[0]):
                        slope = abs(extRight[1]-extLeft[1])/(extRight[0]-extLeft[0])
                #print(slope)

                #cv2.line(frame, (midPoint[0], midPoint[1]), (midPoint[0] + 1, midPoint[1] + 100), (255, 255, 255), 5)
                #angle in Radians with perpendicular
                originalAngleP = np.arctan((slope1 - slope) / (1 + slope1 * slope))
                #angle with Horizontal
                originalAngleH = np.arctan(slope)
                #Angle in degrees
                originalAngleH = originalAngleH*radianToDegree
                originalAngleP=originalAngleP*radianToDegree
                #print(originalAngleP)
                if (abs(originalAngleP) > minAngle and abs(originalAngleH) < maxAngle and abs(originalAngleP)+abs(originalAngleH)>89 and abs(originalAngleP)+abs(originalAngleH)<91):
                    count += 1
                    if (count > 18):
                        count_fall+=1
                        #print("Fall detected")
                        list_falls.append((time.time()))
                        if(count_fall>1):
                            if(list_falls[len(list_falls)-1]-list_falls[len(list_falls)-2]<.5):
                                #print (list_falls[len(list_falls)-1]-list_falls[len(list_falls)-2])
                                #print ("Fall detected")
                                return "Yes"

                            else:
                                continue

                        count = 0

        #cv2.imshow('Frame', frame)
        #cv2.imshow('gray',gray)
        #cv2.imshow('Thresh',thresh)
        #cv2.imshow('FirstFrame',firstFrame)
        '''
        if cv2.waitKey(10) == ord("q"):
            break
        '''

    #print (list_falls)
    cap.release()
    return "No"
    #cv2.waitKey(1)
    #cv2.destroyAllWindows()
    """
