<script setup>
import { ref, onMounted, computed } from "vue";
import { usePlayerStore } from "../stores/player";
import { usePlaceStore } from "../stores/place";

import VehicleController from "./controller/VehicleController.vue";
import MiniMapController from "./controller/MiniMapController.vue";
import PositionController from "./controller/PositionController.vue";
import ControllerContainer from "./controller/ControllerContainer.vue";

const map = ref(null);
const moduleOnLoad = ref(false);
const subModuleOnLoad = ref(false);
const playerStore = usePlayerStore();
const placeStore = usePlaceStore();

const allModuleOnLoad = computed(() => moduleOnLoad.value && subModuleOnLoad.value);

onMounted(() => {
  const mapModule = document.createElement("script");
  mapModule.type = "text/javascript";
  mapModule.src = import.meta.env.VITE_MAP_MODULE_SRC;

  document.head.appendChild(mapModule);

  mapModule.onload = () => {
    map.value = new window.naver.maps.Map("map", {
      center: new window.naver.maps.LatLng(import.meta.env.VITE_DEFAULT_LAT, import.meta.env.VITE_DEFAULT_LNG),
      zoom: 14,
      tileSpare: 5,
    });

    map.value.setCursor("pointer");

    playerStore.setMap(map.value);
    placeStore.setMap(map.value);

    moduleOnLoad.value = true;
  };

  const mapSubModule = document.createElement("script");
  mapSubModule.type = "text/javascript";
  mapSubModule.src = import.meta.env.VITE_MAP_SUB_MODULE_SRC;

  document.head.appendChild(mapSubModule);

  mapSubModule.onload = () => {
    subModuleOnLoad.value = true;
  };
});
</script>

<template>
  <div id="map-container">
    <div id="map"></div>
    <div class="map-controller-container" v-if="allModuleOnLoad">
      <ControllerContainer />
      <MiniMapController />
      <VehicleController />
      <PositionController />
    </div>
  </div>
</template>

<style scoped>
#map {
  width: 100vw;
  height: 100vh;
  z-index: 0;
}
#map-container {
  position: relative;
}
</style>
