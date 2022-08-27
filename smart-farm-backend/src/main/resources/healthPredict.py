#!/usr/bin/env python3
#!/usr/bin/env python3
import tensorflow as tf

# import keras
# from keras.models import load_model
# from keras.models import Sequential, load_model
# from keras.layers import Dense
# from keras import backend as K
# from keras.initializers import Initializer
import numpy as np
import urllib.request
from PIL import Image
from fastapi import FastAPI

app = FastAPI()

@app.get("/diagnostic")
async def diagnosticPlant(url: str):
    return {"diagnostic": diagnosticPlant(url)}

def diagnosticPlant(url):

    class_names = ['arugula_downy_mildew',
                'arugula_powdery_mildew',
                'basil_downy_mildew',
                'basil_healthy',
                'lettuce_bacterial',
                'lettuce_fungus_downy_mildew',
                'lettuce_fungus_powdery_mildew',
                'lettuce_fungus_septoria_blight',
                'lettuce_healthy',
                'spinach_downy_mildew',
                'spinach_stemphylium_leaf_spot',
                'spinach_white_rust']

    modelPath = "./mio_84%.h5"
    url = url
    #modelPath="C:\\Users\\alani\\git\\smartfarm-web\\smart-farm-backend\\target\classes\\mio_84%.h5"
    #url="https://i0.wp.com/images-prod.healthline.com/hlcmsresource/images/AN_images/basil-1296x728-feature.jpg"
    def custom_loss(y_true, y_pred):
        return loss(y_true, y_pred)

    model = tf.keras.models.load_model(
        modelPath, compile=False,
        custom_objects={"custom_loss": custom_loss},
    )
    #model = tf.keras.models.load_model(modelPath,custom_objects=None, compile=True, options=None)

    urllib.request.urlretrieve(
        url,
        "./tmp.jpeg")

    im = Image.open("./tmp.jpeg")
    img = np.array(im.resize((256,256)))
    img_array = tf.keras.preprocessing.image.img_to_array(img)
    img_array = tf.expand_dims(img_array, 0)
    pred = model.predict(img_array)
    print(class_names[np.argmax(pred[0])])
    return class_names[np.argmax(pred[0])]