import tensorflow as tf

# import keras
# from keras.models import load_model
# from keras.models import Sequential, load_model
# from keras.layers import Dense
# from keras import backend as K
# from keras.initializers import Initializer
import numpy as np
import urllib.request
import sys
from PIL import Image

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

enumRes = {
    'arugula_downy_mildew': "DM",
    'arugula_powdery_mildew': "PM",
    'basil_downy_mildew': "DM",
    'basil_healthy': "HT",
    'lettuce_bacterial': "BT",
    'lettuce_fungus_downy_mildew': "DM",
    'lettuce_fungus_powdery_mildew': "PM",
    'lettuce_fungus_septoria_blight':"SB",
    'lettuce_healthy': "HT",
    'spinach_downy_mildew': "DM",
    'spinach_stemphylium_leaf_spot': "SLS",
    'spinach_white_rust': "WR"
}


modelPath = sys.argv[1]
url = sys.argv[2]
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
    "tmp.jpeg")

im = Image.open("tmp.jpeg")
img = np.array(im.resize((256,256)))
img_array = tf.keras.preprocessing.image.img_to_array(img)
img_array = tf.expand_dims(img_array, 0)
pred = model.predict(img_array)
print(class_names[np.argmax(pred[0])])