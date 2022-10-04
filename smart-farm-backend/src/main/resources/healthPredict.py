#!/usr/bin/env python3
#!/usr/bin/env python3
import tensorflow as tf
import numpy as np
import urllib.request
from PIL import Image
from fastapi import FastAPI

app = FastAPI()

@app.get("/diagnostic")
async def diagnosticPlant(url: str):
    return {"diagnostic": diagnosticPlant(url)}

def diagnosticPlant(url):

    class_names = [
        {"desc": 'arugula_downy_mildew', "enum": "DM"},
        {"desc": 'arugula_powdery_mildew', "enum": "PM"},
        {"desc": 'basil_healthy', "enum": "HT"},
        {"desc": 'lettuce_bacterial', "enum": "BT"},
        {"desc": 'lettuce_fungus_downy_mildew', "enum": "DM"},
        {"desc": 'lettuce_fungus_powdery_mildew', "enum": "PM"},
        {"desc": 'lettuce_fungus_septoria_blight', "enum": "SB"},
        {"desc": 'lettuce_healthy', "enum": "HT"},
        {"desc": 'spinach_downy_mildew', "enum": "DM"},
        {"desc": 'arugula_downy_mildew', "enum": "DM"},
        {"desc": 'spinach_stemphylium_leaf_spot', "enum": "SLS"},
        {"desc": 'spinach_white_rust', "enum": "WR"}
    ]

    modelPath = "./mio_84%.h5"

    def custom_loss(y_true, y_pred):
        return loss(y_true, y_pred)

    model = tf.keras.models.load_model(
        modelPath, compile=False,
        custom_objects={"custom_loss": custom_loss},
    )

    urllib.request.urlretrieve(
        url,
        "./tmp.jpeg")

    im = Image.open("./tmp.jpeg")
    img = np.array(im.resize((256,256)))
    img_array = tf.keras.preprocessing.image.img_to_array(img)
    img_array = tf.expand_dims(img_array, 0)
    pred = model.predict(img_array)
    res = class_names[np.argmax(pred[0])]
    print("Description:{}, Enum:{}".format(res["desc"],res["enum"]))
    return res["enum"]