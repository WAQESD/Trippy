import { defineStore } from "pinia";
import { ref } from "vue";

export const useModalStore = defineStore("modal", () => {
  const isActive = ref(false);
  const content = ref(null);
  const props = ref({});

  function setModal(state, component = null, propsObj = null) {
    isActive.value = state;
    content.value = component;
    props.value = propsObj;
  }

  return { isActive, content, props, setModal };
});
